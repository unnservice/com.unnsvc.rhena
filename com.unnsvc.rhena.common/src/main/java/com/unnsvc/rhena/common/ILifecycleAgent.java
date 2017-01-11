
package com.unnsvc.rhena.common;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ILifecycleAgent extends Remote {

	// public ILifecycle buildLifecycle(ILifecycleBuilder lifecycleBuilder,
	// IEntryPoint entryPoint, String lifecycleName) throws RemoteException;

	// public File executeLifecycle(ILifecycle lifecycle, IRhenaModule module,
	// EExecutionType executionType, IDependencies dependencies) throws
	// RemoteException;

	public File executeLifecycle(ILifecycleExecutable lifecycleExecutable, IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RemoteException;

}
