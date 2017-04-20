
package com.unnsvc.rhena.execution.requests;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionResult implements IExecutionResult {

	private EExecutionType type;
	private IRhenaModule module;

	public ExecutionResult(EExecutionType type, IRhenaModule module) {

		this.type = type;
		this.module = module;
	}

	@Override
	public EExecutionType getType() {

		return type;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}
}
