
package com.unnsvc.rhena.common.execution;

public interface IModuleExecutorCallback {

	public void onExecuted(IExecutionResponse executionResult);

	public void onException(Throwable t);

}
