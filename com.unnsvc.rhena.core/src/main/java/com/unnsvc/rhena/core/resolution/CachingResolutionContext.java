
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent.EAddRemove;

/**
 * This resolution context will first attempt to resolve the artifact form the
 * local cache repository. If it doesn't exist there then try with all other
 * repositories
 * 
 * @author noname
 *
 */
public class CachingResolutionContext extends AbstractResolutionContext {
//
//	private IRhenaLogger log;
//	private LocalCacheRepository cacheRepository;
//	/**
//	 * @TODO create an implementation which has listener support, or some sort
//	 *       of observation mechanism so we can observe all add/remove inside
//	 *       this map
//	 */
//	protected Map<ModuleIdentifier, IRhenaModule> modelCache;
//
//	/**
//	 * This will become modified from multiple threads as materialiseExecutor is
//	 * called from execution threads.
//	 * 
//	 * @TODO take it out of here and into a rhena context
//	 */
//	protected Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executionCache;
//	private Set<IRhenaEdge> edgeCache;
//
//	public CachingResolutionContext(RhenaConfiguration configuration) {
//
//		this.cacheRepository = new LocalCacheRepository(this, configuration.getLocalCacheRepository());
//		this.modelCache = new ConcurrentHashMap<ModuleIdentifier, IRhenaModule>();
//		this.executionCache = new ConcurrentHashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
//		this.edgeCache = Collections.synchronizedSet(new HashSet<IRhenaEdge>());
//		this.log = getLogger(getClass());
//	}
//
//	@Override
//	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {
//
//		if (repositories.isEmpty()) {
//			throw new NotExistsException("No repository in context.");
//		}
//
//		IRhenaModule module = modelCache.get(moduleIdentifier);
//
//		if (module == null) {
//			module = cacheRepository.materialiseModel(moduleIdentifier);
//
//			if (module == null) {
//
//				module = super.materialiseModel(moduleIdentifier);
//				if (module == null) {
//
//					throw new RhenaException(moduleIdentifier.toTag(EExecutionType.MODEL) + " failed to resolve");
//				}
//			}
//
//			log.info(moduleIdentifier, EExecutionType.MODEL, "materialised");
//			modelCache.put(moduleIdentifier, module);
//			fireEvent(new ModuleAddRemoveEvent(moduleIdentifier, EAddRemove.ADDED));
//		}
//
//		return module;
//	}
//
//	@Override
//	public IRhenaModule materialiseWorkspaceModel(String projectName) throws RhenaException {
//
//		// @todo check for workspace repositories instead
//		if (repositories.isEmpty()) {
//			throw new NotExistsException("No repository in context.");
//		}
//
//		for (IRepository repository : repositories) {
//
//			if (repository instanceof WorkspaceRepository) {
//				WorkspaceRepository repo = (WorkspaceRepository) repository;
//				File workspaceProject = new File(repo.getWorkspaceDirectory(), projectName);
//				if (!workspaceProject.isDirectory()) {
//					continue;
//				}
//				ModuleIdentifier moduleIdentifier = Utils.readModuleIdentifier(workspaceProject);
//				return materialiseModel(moduleIdentifier);
//			}
//		}
//
//		throw new NotExistsException("Project: " + projectName + " was not found in defined workspace repositories.");
//	}
//
//	@Override
//	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {
//
//		ModuleIdentifier identifier = module.getModuleIdentifier();
//		if (executionCache.get(identifier) != null && executionCache.get(identifier).get(type) != null) {
//			return executionCache.get(identifier).get(type);
//		}
//
//		IRhenaExecution execution = null;
//
//		try {
//			execution = cacheRepository.materialiseExecution(module, type);
//		} catch (NotExistsException nee) {
//			log.debug(module.getModuleIdentifier(), type, "Not in local cache repository: " + module.getModuleIdentifier().toTag(type));
//		}
//
//		if (execution == null) {
//
//			execution = super.materialiseExecution(module, type);
//
//			if (execution == null) {
//				throw new RhenaException(identifier.toTag(type) + " failed to resolve execution");
//			}
//		}
//
//		if (executionCache.containsKey(identifier)) {
//
//			executionCache.get(identifier).put(type, execution);
//		} else {
//			Map<EExecutionType, IRhenaExecution> typeExecutions = new EnumMap<EExecutionType, IRhenaExecution>(EExecutionType.class);
//			typeExecutions.put(type, execution);
//			executionCache.put(identifier, typeExecutions);
//		}
//
//		log.info(module.getModuleIdentifier(), type, " materialised " + execution.getArtifact());
//
//		return execution;
//	}
//
//	@Override
//	public void dropFromCache(ModuleIdentifier identifier) {
//		
//		
//	}
//
//	@Override
//	public Set<IRhenaEdge> getEdges() {
//
//		return edgeCache;
//	}

}
