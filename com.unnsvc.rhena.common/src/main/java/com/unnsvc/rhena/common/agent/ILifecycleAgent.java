
package com.unnsvc.rhena.common.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ILifecycleAgent extends Remote {

	public ILifecycleExecutionResult executeLifecycle(ILifecycleExecutable lifecycleExecutable, IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RemoteException;

}
