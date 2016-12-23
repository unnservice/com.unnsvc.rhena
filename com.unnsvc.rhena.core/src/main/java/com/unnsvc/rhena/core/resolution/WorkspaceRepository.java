
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
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

		return runInExecutableLifecycle(cache, entryPoint);
	}

	private IRhenaExecution runInExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

//		IRhenaModule module = cache.getModule(entryPoint.getTarget());
//		
//		ExecutableLifecycle execLifecycle = new ExecutableLifecycle();
//
//		// This contains basically just ILifecycleProcessorReference, so they
//		// can be processed uniformly
//		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());
//
//		IExecutionReference contextRef = lifecycleRef.getContext();
//
//		for (IProcessorReference processorRef : lifecycleRef.getProcessors()) {
//
//		}
//		IGeneratorReference generatorRef = lifecycleRef.getGenerator();

		return new RhenaExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(), "http://not.implemented", "not-implemented"));
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
