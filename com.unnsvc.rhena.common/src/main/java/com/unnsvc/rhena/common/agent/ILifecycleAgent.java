
package com.unnsvc.rhena.common.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILifecycleAgent extends Remote {

	public ILifecycleExecutionResult executeLifecycle(IExecutionRequest request) throws RemoteException;

}
