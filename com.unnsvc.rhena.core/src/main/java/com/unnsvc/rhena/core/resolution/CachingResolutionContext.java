
package com.unnsvc.rhena.core.resolution;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;

/**
 * This resolution context will first attempt to resolve the artifact form the
 * local cache repository. If it doesn't exist there then try with all other
 * repositories
 * 
 * @author noname
 *
 */
public class CachingResolutionContext extends AbstractResolutionContext {

	private Logger log = LoggerFactory.getLogger(getClass());
	private LocalCacheRepository cacheRepository;
	protected Map<ModuleIdentifier, IRhenaModule> models;
	/**
	 * This will become modified from multiple threads as materialiseExecutor is
	 * called from execution threads.
	 * @TODO take it out of here and into a rhena context
	 */
	protected Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private Set<IRhenaEdge> edges;

	public CachingResolutionContext(RhenaConfiguration configuration) {

		this.cacheRepository = new LocalCacheRepository(this, configuration.getLocalCacheRepository());
		this.models = new ConcurrentHashMap<ModuleIdentifier, IRhenaModule>();
		this.executions = new ConcurrentHashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		this.edges = Collections.synchronizedSet(new HashSet<IRhenaEdge>());
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		if (repositories.isEmpty()) {
			throw new NotExistsException("No repository in context.");
		}
		
		IRhenaModule module = models.get(moduleIdentifier);

		if (module == null) {
			module = cacheRepository.materialiseModel(moduleIdentifier);

			if (module == null) {

				module = super.materialiseModel(moduleIdentifier);
				if (module == null) {

					throw new RhenaException(moduleIdentifier.toTag(EExecutionType.MODEL) + " failed to resolve");
				}
			}

			log.info("[" + moduleIdentifier + "]:model materialised");
			models.put(moduleIdentifier, module);
		}

		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		ModuleIdentifier identifier = module.getModuleIdentifier();
		if (executions.get(identifier) != null && executions.get(identifier).get(type) != null) {
			return executions.get(identifier).get(type);
		}

		IRhenaExecution execution = null;

		try {
			execution = cacheRepository.materialiseExecution(module, type);
		} catch (NotExistsException nee) {
			log.debug("Not in local cache repository: " + module.getModuleIdentifier().toTag(type));
		}

		if (execution == null) {

			execution = super.materialiseExecution(module, type);

			if (execution == null) {
				throw new RhenaException(identifier.toTag(type) + " failed to resolve execution");
			}
		}

		if (executions.containsKey(identifier)) {

			executions.get(identifier).put(type, execution);
		} else {
			Map<EExecutionType, IRhenaExecution> typeExecutions = new EnumMap<EExecutionType, IRhenaExecution>(EExecutionType.class);
			typeExecutions.put(type, execution);
			executions.put(identifier, typeExecutions);
		}

		log.info(module.getModuleIdentifier().toTag(type) + " materialised " + execution.getArtifact());

		return execution;
	}

	@Override
	public Set<IRhenaEdge> getEdges() {

		return edges;
	}
}
