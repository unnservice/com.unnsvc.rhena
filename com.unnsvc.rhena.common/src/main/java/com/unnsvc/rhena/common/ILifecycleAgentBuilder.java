
package com.unnsvc.rhena.common;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface ILifecycleAgentBuilder extends Remote {

	public void agentNotify() throws RemoteException;

	public void startup() throws RhenaException, IOException, InterruptedException, NotBoundException;

	public ILifecycleAgent getLifecycleAgent() throws RemoteException;

	public void shutdown() throws AccessException, RemoteException, NotBoundException;

}
