
package com.unnsvc.rhena.common.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ILifecycleAgent extends Remote {

	public ILifecycleExecutionResult executeLifecycle(ICaller caller, IRhenaModule module, ILifecycleExecutable lifecycleExecutable, IDependencies dependencies) throws RemoteException;

}
