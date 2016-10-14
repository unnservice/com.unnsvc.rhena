
package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
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
	protected Map<ModuleIdentifier, Map<ExecutionType, IRhenaExecution>> executions;

	public CachingResolutionContext(RhenaConfiguration configuration) {

		this.cacheRepository = new LocalCacheRepository(configuration.getLocalCacheRepository());
		this.models = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.executions = new HashMap<ModuleIdentifier, Map<ExecutionType, IRhenaExecution>>();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		IRhenaModule module = models.get(moduleIdentifier);

		if (module == null) {
			module = cacheRepository.materialiseModel(moduleIdentifier);

			if (module == null) {

				module = super.materialiseModel(moduleIdentifier);
				if (module == null) {

					throw new RhenaException(moduleIdentifier.toTag(ExecutionType.MODEL) + " failed to resolve");
				}
			}

			log.info("[" + moduleIdentifier + "]:model materialised");
			models.put(moduleIdentifier, module);
		}

		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, ExecutionType type) throws RhenaException {

		ModuleIdentifier identifier = module.getModuleIdentifier();
		if (executions.get(identifier) != null && executions.get(identifier).get(type) != null) {
			return executions.get(identifier).get(type);
		}

		IRhenaExecution execution = cacheRepository.materialiseExecution(module, type);

		if (execution == null) {

			execution = super.materialiseExecution(module, type);

			if (execution == null) {
				throw new RhenaException(identifier.toTag(type) + " failed to resolve execution");
			}
		}

		if (executions.containsKey(identifier)) {

			executions.get(identifier).put(type, execution);
		} else {
			Map<ExecutionType, IRhenaExecution> typeExecutions = new HashMap<ExecutionType, IRhenaExecution>();
			typeExecutions.put(type, execution);
			executions.put(identifier, typeExecutions);
		}

		log.info(module.getModuleIdentifier().toTag(type) + " materialised " + execution.getArtifact());
		
		return execution;
	}
}
