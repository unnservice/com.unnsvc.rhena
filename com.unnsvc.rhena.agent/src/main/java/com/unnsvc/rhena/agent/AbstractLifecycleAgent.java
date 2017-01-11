
package com.unnsvc.rhena.agent;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.unnsvc.rhena.common.ILifecycleAgent;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;

public abstract class AbstractLifecycleAgent extends UnicastRemoteObject implements ILifecycleAgent {

	private static final long serialVersionUID = 1L;
	private Registry registry;

	protected AbstractLifecycleAgent() throws RemoteException {

		super();
	}

	/**
	 * Entry point for when executing within a separate agent process
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {

		int port = Integer.valueOf(args[0]);
		System.err.println("Starting up agent with classpath: " + System.getProperty("java.class.path"));

		AbstractLifecycleAgent selfInstance = new LifecycleAgent();
		selfInstance.setupRegistry(port);
		selfInstance.notifyServer();
	}

	private void setupRegistry(int port) throws RemoteException, AlreadyBoundException {

		if (System.getSecurityManager() == null) {
			// System.setSecurityManager(new RMISecurityManager());
			// System.setSecurityManager(new SecurityManager());
			System.setSecurityManager(new PermitAllSecurityManager());
		}
		registry = LocateRegistry.getRegistry(port);
		registry.bind(ILifecycleAgent.class.getName(), (ILifecycleAgent) this);
	}

	private void notifyServer() throws RemoteException, NotBoundException {

		ILifecycleAgentBuilder server = (ILifecycleAgentBuilder) registry.lookup(ILifecycleAgentBuilder.class.getName());
		server.agentNotify();
	}
	
	protected Object getRemoteType(Class<?> clazz) throws AccessException, RemoteException, NotBoundException {

		return registry.lookup(clazz.getName());
	}
}
