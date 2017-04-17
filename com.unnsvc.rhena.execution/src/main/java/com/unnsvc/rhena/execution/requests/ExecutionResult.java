
package com.unnsvc.rhena.execution.requests;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;

public class ExecutionResult implements IExecutionResult {

	private ModuleIdentifier identifier;
	private EExecutionType type;

	public ExecutionResult(EExecutionType type, ModuleIdentifier identifier) {

		this.type = type;
		this.identifier = identifier;
	}

	@Override
	public EExecutionType getType() {

		return type;
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

}
