
package com.unnsvc.rhena.execution.requests;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionRequest implements IExecutionRequest {

	private static final long serialVersionUID = 1L;

	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public ExecutionRequest(IEntryPoint entryPoint, IRhenaModule module) {

		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}

}
