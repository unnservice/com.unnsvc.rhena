
package com.unnsvc.rhena.common;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface ILifecycleAgentManager extends Remote {

	/**
	 * Called by agent upon startup to notify that it has started up
	 * 
	 * @throws RemoteException
	 */
	public void agentNotify() throws RemoteException;

	public void startup() throws RhenaException, IOException, InterruptedException, NotBoundException;

	public ILifecycleAgent getLifecycleAgent() throws RemoteException;

	public void export(String typeName, Remote object) throws AccessException, RemoteException, AlreadyBoundException;

}
