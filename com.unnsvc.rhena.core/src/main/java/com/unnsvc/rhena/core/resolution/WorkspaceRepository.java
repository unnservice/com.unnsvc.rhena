
package com.unnsvc.rhena.core.resolution;

import java.io.File;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;
import com.unnsvc.rhena.core.lifecycle.Lifecycle;
import com.unnsvc.rhena.core.lifecycle.LifecycleEngine;

public class WorkspaceRepository extends AbstractWorkspaceRepository {

	private LifecycleEngine lifecycleEngine;

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
		this.lifecycleEngine = new LifecycleEngine();
	}

	@Override
	public IRhenaExecution materialiseExecution(IModelResolver resolver, IEntryPoint entryPoint) throws RhenaException {

//		config.getLogger(getClass()).info(entryPoint.getTarget(), "Materialise execution");
		IRhenaModule module = resolver.materialiseModel(entryPoint.getTarget());
//		ILifecycle lifecycle = createLifecycle(resolver, module);
		
//		module.visit(new DepchainVisitor(resolver, entryPoint.getExecutionType()));
		
//		System.err.println("Target: " + entryPoint.getTarget());
		ModuleIdentifier identifier = entryPoint.getTarget();
		return new RhenaExecution(identifier, entryPoint.getExecutionType(), new ArtifactDescriptor(identifier.toString(), "http://not.implemented", "not-implemented"));
	}

	/**
	 * We produce a lifecycle that is callable, and alter deliver to it the
	 * dependency chain
	 * 
	 * @param resolver
	 * @param module
	 * @return
	 */
	private ILifecycle createLifecycle(IModelResolver resolver, IRhenaModule module) {

		Lifecycle lifecycle = new Lifecycle();

		return lifecycle;
	}
}
