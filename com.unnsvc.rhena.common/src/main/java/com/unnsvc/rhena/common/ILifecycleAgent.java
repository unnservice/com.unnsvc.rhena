
package com.unnsvc.rhena.common;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ILifecycleAgent extends Remote {

	public void shutdown() throws RemoteException;

	public ILifecycle buildLifecycle(ILifecycleBuilder lifecycleBuilder, IEntryPoint entryPoint, String lifecycleName) throws RemoteException;

	public File executeLifecycle(ILifecycle lifecycle, IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RemoteException;

}
