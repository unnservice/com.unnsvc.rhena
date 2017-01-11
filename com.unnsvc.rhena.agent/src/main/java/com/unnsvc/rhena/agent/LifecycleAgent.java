
package com.unnsvc.rhena.agent;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.unnsvc.rhena.common.ILifecycleAgent;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;
import com.unnsvc.rhena.common.ILifecycleBuilder;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;

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

		LifecycleAgent selfInstance = new LifecycleAgent();
		selfInstance.setupRegistry();
		selfInstance.notifyServer();
	}

	private void setupRegistry() throws RemoteException, AlreadyBoundException {

		registry = LocateRegistry.getRegistry(RhenaConstants.DEFAULT_LIFECYCLE_AGENT_PORT);
		registry.bind(ILifecycleAgent.class.getName(), this);
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
}
