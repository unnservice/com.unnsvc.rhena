
package com.unnsvc.rhena.agent;

import java.io.File;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.unnsvc.rhena.common.ILifecycleAgent;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;
import com.unnsvc.rhena.common.ILifecycleBuilder;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.visitors.IDependencies;

/**
 * This agent is executed in a separate agent JVM
 * 
 * @author noname
 *
 */
public class LifecycleAgent extends UnicastRemoteObject implements ILifecycleAgent {

	private static final long serialVersionUID = 1L;
	private Registry registry;

	public LifecycleAgent() throws RemoteException {

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

		LifecycleAgent selfInstance = new LifecycleAgent();
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

	@Override
	public void shutdown() throws RemoteException {

		try {
			registry.unbind(ILifecycleAgent.class.getName());
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NotBoundException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public ILifecycle buildLifecycle(ILifecycleBuilder lifecycleBuilder, IEntryPoint entryPoint, String lifecycleName) throws RhenaException {

		// LifecycleBuilder lifecycleBuilder = new LifecycleBuilder(module,
		// context);
		return lifecycleBuilder.buildLifecycle(entryPoint, lifecycleName);
	}

	@Override
	public File executeLifecycle(ILifecycle lifecycle, IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RemoteException {

		try {

			return lifecycle.executeLifecycle(module, executionType, dependencies);
		} catch (Throwable ex) {
			throw new RemoteException(ex.getMessage(), ex);
		}
	}
}
