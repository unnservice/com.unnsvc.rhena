
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;
import com.unnsvc.rhena.core.visitors.DependencyCollector;

public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		Map<EExecutionType, List<IRhenaExecution>> deps = new EnumMap<EExecutionType, List<IRhenaExecution>>(EExecutionType.class);
		for(EExecutionType et : EExecutionType.values()) {
			deps.put(et, new ArrayList<IRhenaExecution>());
		}

		// get dependency chains of dependencies
		getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

		// get the other executions of this module into the dependencies
		for(EExecutionType depEt : entryPoint.getExecutionType().getTraversables()) {
			
			IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(depEt);
			if(deps.get(depEt).contains(exec)) {
				deps.get(depEt).remove(exec);
			}
			deps.get(depEt).add(exec);
		}

		// debug dependency chains
		deps.forEach((key, val) -> 
			val.forEach(exec ->
				config.getLogger(getClass()).debug(key + ": " + exec)
			)
		);

		IRhenaModule module = cache.getModule(entryPoint.getTarget());
		if(module.getLifecycleName() != null && module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {
			
			return runInExecutableLifecycle(cache, entryPoint, module);
		} else {
			
			return runInDefaultExecutableLifecycle(cache, entryPoint, module);
		}
	}

	private IRhenaExecution runInDefaultExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		
		return new RhenaExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(), "http://not.implemented", "not-implemented"));
	}

	private IRhenaExecution runInExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {
		
		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());

		IExecutionContext context = constructLifecycleProcessor(cache, lifecycleRef.getContext(), IExecutionContext.class, new Class[] {IRhenaCache.class}, cache);
		context.configure(module, lifecycleRef.getContext().getConfiguration());

		return new RhenaExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(), "http://not.implemented", "not-implemented"));
	}

	/**
	 * @TODO checks for constructor validity and type conformance, gc classloaders properly
	 * @param cache
	 * @param processor
	 * @param type
	 * @return
	 * @throws RhenaException
	 */
	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T constructLifecycleProcessor(IRhenaCache cache, ILifecycleProcessorReference processor, Class<T> type, Class<?>[] argTypes, Object... args) throws RhenaException {
		
		DependencyCollector coll = new DependencyCollector(cache, processor.getModuleEdge());
		List<URL> deps = new ArrayList<URL>();
		for(IRhenaExecution exec : coll.getDependencies()) {
			deps.add(exec.getArtifact().getArtifactUrl());
		}
		
		/**
		 * @TODO Resource leak, refactor code to close the classloader eventually...
		 */
		URLClassLoader urlc = new URLClassLoader(deps.toArray(new URL[deps.size()]));
		
		try {
			Class<T> clazz = (Class<T>) urlc.loadClass(processor.getClazz());
			Constructor<T> constr = clazz.getConstructor(argTypes);
			return constr.newInstance(args);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private void getDepchain(Map<EExecutionType, List<IRhenaExecution>> deps, IRhenaCache cache, ModuleIdentifier identifier, EExecutionType et) throws RhenaException {
		
		IRhenaModule module = cache.getModule(identifier);

		/**
		 * Collect dependency information
		 */
		for (IRhenaEdge edge : module.getDependencies()) {
			IRhenaModule depmod = cache.getModule(identifier);
			DependencyCollector coll = new DependencyCollector(cache, edge);
			depmod.visit(coll);
			
			for(IRhenaExecution exec : coll.getDependencies()) {
				if(deps.get(edge.getEntryPoint().getExecutionType()).contains(exec)) {
					deps.get(edge.getEntryPoint().getExecutionType()).remove(exec);
				}
				deps.get(edge.getEntryPoint().getExecutionType()).add(exec);
			}			
		}
	}
}
