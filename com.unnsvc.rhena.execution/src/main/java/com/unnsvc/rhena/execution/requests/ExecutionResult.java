
package com.unnsvc.rhena.execution.requests;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionResult implements IExecutionResult {

	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public ExecutionResult(IEntryPoint entryPoint, IRhenaModule module) {

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
