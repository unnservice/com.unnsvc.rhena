
package com.unnsvc.rhena.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;

public interface ILifecycleAgent extends Remote {

	public void shutdown() throws RemoteException;

	public ILifecycle buildLifecycle(ILifecycleBuilder lifecycleBuilder, IEntryPoint entryPoint, String lifecycleName) throws RemoteException;

}
