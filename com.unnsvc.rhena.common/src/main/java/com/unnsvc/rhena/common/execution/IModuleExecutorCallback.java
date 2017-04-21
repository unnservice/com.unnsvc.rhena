
package com.unnsvc.rhena.common.execution;

public interface IModuleExecutorCallback {

	public void onExecuted(IExecutionResult executionResult);

	public void onException(Exception ex);

}
