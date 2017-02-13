
package com.unnsvc.rhena.common.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;

public interface ILifecycleAgent extends Remote {

	public ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable) throws RemoteException;

}
