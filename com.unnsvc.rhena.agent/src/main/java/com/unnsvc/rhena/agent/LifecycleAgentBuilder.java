
package com.unnsvc.rhena.agent;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.ILifecycleAgent;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;

public class LifecycleAgentBuilder extends UnicastRemoteObject implements ILifecycleAgentBuilder {

	private static final long serialVersionUID = 1L;
	private Registry registry;
	private boolean notifiedByAgent = false;
	private ILifecycleAgent lifecycleAgent;
	private Process lifecycleAgentProcess;
	private IRhenaConfiguration config;
	private ILogger logger;

	public LifecycleAgentBuilder(IRhenaConfiguration config, ILogger logFacade) throws RemoteException, RhenaException {

		super();
		this.config = config;
		this.logger = logFacade;
	}

	/**
	 * Used just for testing
	 * 
	 * @param args
	 * @throws Exception
	 */
	// public static void main(String... args) throws Exception {
	//
	// LifecycleAgentBuilder m = new LifecycleAgentBuilder();
	// m.startup();
	//
	// m.shutdown();
	// }

	@Override
	public void startup() throws RhenaException, IOException, InterruptedException, NotBoundException {

		// System.err.println("Security manager " +
		// System.getSecurityManager());
		// if (System.getSecurityManager() == null) {
		//// System.setSecurityManager(new RMISecurityManager());
		// System.setSecurityManager(new SecurityManager());
		// }

		createRegistry();
		start();
	}

	private void createRegistry() throws RhenaException {

		/**
		 * Create registry
		 */
		try {

			registry = LocateRegistry.createRegistry(RhenaConstants.DEFAULT_LIFECYCLE_AGENT_PORT);
			registry.rebind(ILifecycleAgentBuilder.class.getName(), (ILifecycleAgentBuilder) this);
			logger.info(getClass(), "Started registry: " + registry);
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
		cmd.add("-classpath");
		cmd.add(config.getAgentClasspath());
		cmd.add(LifecycleAgent.class.getName());

		ProcessBuilder builder = new ProcessBuilder(cmd);

		System.err.println("Starting lifecycle agent: " + builder.command());
		logger.info(getClass(), "Building process: " + builder.command());

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

	// private String createPrefixed(String agentClasspath) {

	// StringBuilder sb = new StringBuilder();
	// for (String path : agentClasspath.split(File.pathSeparator)) {
	// sb.append("file://" + path).append(" ");
	// }
	// return "\"" + sb.toString().substring(0, sb.toString().length() - 1) +
	// "\"";
	// return
	// "file:///data/storage/sources/com.unnsvc/com.unnsvc.rhena/com.unnsvc.rhena.common/target/classes/";
	// }

	@Override
	public void shutdown() throws AccessException, RemoteException, NotBoundException {

		lifecycleAgent = null;

		registry.unbind(ILifecycleAgentBuilder.class.getName());
		UnicastRemoteObject.unexportObject(this, true);

		lifecycleAgentProcess.destroy();
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
}
