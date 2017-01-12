
package com.unnsvc.rhena.agent;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.ILifecycleAgent;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class LifecycleAgentManager extends UnicastRemoteObject implements ILifecycleAgentBuilder {

	private static final long serialVersionUID = 1L;
	private Registry registry;
	private boolean notifiedByAgent = false;
	private ILifecycleAgent lifecycleAgent;
	private Process lifecycleAgentProcess;
	private int rmiRegistryPort = 0;
	private String lifecycleAgentClasspath;
	private String profilerClasspath;

	public LifecycleAgentManager(String lifecycleAgentClasspath, String profilerClasspath) throws RemoteException, RhenaException {

		super();
		this.lifecycleAgentClasspath = lifecycleAgentClasspath;
		this.profilerClasspath = profilerClasspath;
	}

	@Override
	public void startup() throws RhenaException, IOException, InterruptedException, NotBoundException {

		createRegistry();
		start();
	}

	private void createRegistry() throws RhenaException {

		/**
		 * Create registry
		 */
		try {
			ServerSocket ss = new ServerSocket(0);
			ss.close();
			rmiRegistryPort = ss.getLocalPort();
			registry = LocateRegistry.createRegistry(rmiRegistryPort);
			registry.rebind(ILifecycleAgentBuilder.class.getName(), (ILifecycleAgentBuilder) this);
			// logger.info(getClass(), "Started registry: " + registry);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

				public void run() {

					try {

						UnicastRemoteObject.unexportObject(registry, true);
						lifecycleAgentProcess.destroyForcibly();
					} catch (Exception ex) {
						// When list() fails
						ex.printStackTrace();
					}
				}
			}));
		} catch (Exception e) {
			throw new RhenaException("Failed to create RMI registry", e);
		}
	}

	private void start() throws IOException, InterruptedException, RhenaException, NotBoundException {

		String javaExecutable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

		List<String> cmd = new ArrayList<String>();
		cmd.add(javaExecutable);
		// cmd.add("-Djava.rmi.server.codebase=" +
		// createPrefixed(config.getAgentClasspath()));
		// cmd.add(config.getAgentClasspath());
		if (lifecycleAgentClasspath != null) {
			cmd.add("-classpath");
			cmd.add(lifecycleAgentClasspath);
		}
		if(profilerClasspath != null) {
			cmd.add("-javaagent:" + profilerClasspath);
		}
		cmd.add(LifecycleAgent.class.getName());
		cmd.add(rmiRegistryPort + "");

		ProcessBuilder builder = new ProcessBuilder(cmd);

		System.err.println("Starting lifecycle agent: " + builder.command());
		// logger.info(getClass(), "Building process: " + builder.command());

		lifecycleAgentProcess = builder.inheritIO().start();

		synchronized (this) {
			/**
			 * Sleep until the agent is ready
			 */
			wait(5000);

			if (!notifiedByAgent) {
				throw new RhenaException("Timeout reached, failed to start lifecycle agent");
			}
		}

		/**
		 * Now obtain the remoting interface
		 */
		Object remoteObj = registry.lookup(ILifecycleAgent.class.getName());
		System.err.println("Remote object is: " + remoteObj);
		this.lifecycleAgent = (ILifecycleAgent) registry.lookup(ILifecycleAgent.class.getName());
		System.out.println("Retrieved lifecycle agent in child process: " + lifecycleAgent);
	}

	@Override
	public void agentNotify() {

		synchronized (this) {
			this.notifiedByAgent = true;
			notifyAll();
		}
	}

	@Override
	public ILifecycleAgent getLifecycleAgent() {

		return lifecycleAgent;
	}

	@Override
	public void export(String typeName, Remote object) throws AccessException, RemoteException, AlreadyBoundException {

		registry.bind(typeName, object);
	}
}
