
package com.unnsvc.rhena.execution.requests;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class ExecutionRequest implements IExecutionRequest {

	private static final long serialVersionUID = 1L;

	private IEntryPoint entryPoint;
	private IRhenaModule module;
	private ILifecycleInstance lifecycle;
	private IDependencies dependencies;

	public ExecutionRequest(IEntryPoint entryPoint, IRhenaModule module, ILifecycleInstance lifecycle, IDependencies dependencies) {

		this.entryPoint = entryPoint;
		this.module = module;
		this.lifecycle = lifecycle;
		this.dependencies = dependencies;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}

	@Override
	public ILifecycleInstance getLifecycle() {

		return lifecycle;
	}

	@Override
	public IDependencies getDependencies() {

		return dependencies;
	}
}
