package com.unnsvc.rhena.execution;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.execution.IModuleExecutorCallback;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModuleExecutionCallback implements IModuleExecutorCallback {

	private ModuleIdentifier identifier;
	
	public ModuleExecutionCallback(ModuleIdentifier identifier) {
		
		this.identifier = identifier;
	}

	@Override
	public void onExecuted(IExecutionResult executionResult) {

	}
	
	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
	}
}
