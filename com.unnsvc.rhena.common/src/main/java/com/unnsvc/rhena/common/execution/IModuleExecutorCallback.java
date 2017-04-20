
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IModuleExecutorCallback {

	public void onExecuted(IExecutionResult executionResult);

	public ModuleIdentifier getIdentifier();

}
