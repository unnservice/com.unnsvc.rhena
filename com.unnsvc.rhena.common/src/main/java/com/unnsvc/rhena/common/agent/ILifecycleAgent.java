
package com.unnsvc.rhena.common.agent;

import java.rmi.Remote;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface ILifecycleAgent extends Remote {

	public ILifecycleExecutionResult executeLifecycle(IExecutionRequest request) throws RhenaException;

}
