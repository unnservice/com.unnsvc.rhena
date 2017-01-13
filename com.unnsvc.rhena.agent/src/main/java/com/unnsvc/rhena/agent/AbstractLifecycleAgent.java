
package com.unnsvc.rhena.agent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.unnsvc.rhena.common.agent.ILifecycleAgent;
import com.unnsvc.rhena.common.agent.ILifecycleAgentManager;

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
		int timeout = 5000;
		RMISocketFactory fact = new RMISocketFactory() {

			@Override
			public Socket createSocket(String host, int port) throws IOException {

				Socket socket = new Socket();
				socket.setSoTimeout(timeout);
				socket.setSoLinger(false, 0);
				socket.connect(new InetSocketAddress(host, port), timeout);
				return socket;
			}

			@Override
			public ServerSocket createServerSocket(int port) throws IOException {

				return new ServerSocket(port);
			}
		};

		registry = LocateRegistry.getRegistry(null, port, fact);
		registry.bind(ILifecycleAgent.class.getName(), this);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {

				try {

					UnicastRemoteObject.unexportObject(AbstractLifecycleAgent.this, true);
				} catch (Exception ex) {
					// When list() fails
					ex.printStackTrace();
				}
			}
		}));
	}

	private void notifyServer() throws RemoteException, NotBoundException {

		ILifecycleAgentManager server = (ILifecycleAgentManager) registry.lookup(ILifecycleAgentManager.class.getName());
		server.agentNotify();
	}

	protected Object getRemoteType(String typeName) throws AccessException, RemoteException, NotBoundException {

		return registry.lookup(typeName);
	}
}
