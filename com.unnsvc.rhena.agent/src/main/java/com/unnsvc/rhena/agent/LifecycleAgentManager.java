
package com.unnsvc.rhena.agent;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.agent.ILifecycleAgent;
import com.unnsvc.rhena.common.agent.ILifecycleAgentManager;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.process.IProcessListener;
import com.unnsvc.rhena.common.process.ProcessExitTracker;
import com.unnsvc.rhena.profiling.IClassLoaderReporting;
import com.unnsvc.rhena.profiling.report.IDiagnosticReport;

public class LifecycleAgentManager extends UnicastRemoteObject implements ILifecycleAgentManager {

	private static final long serialVersionUID = 1L;
	private Registry registry;
	private boolean notifiedByAgent = false;
	private ILifecycleAgent lifecycleAgent;
	private Process lifecycleAgentProcess;
	private int rmiRegistryPort = 0;
	private ProcessExitTracker lifecycleAgentProcessExitTracker;
	private IRhenaConfiguration config;
	private ILogger logger;

	public LifecycleAgentManager(ILogger logger, IRhenaConfiguration config) throws RemoteException, RhenaException {

		super();
		this.logger = logger;
		this.config = config;
	}

	@Override
	public synchronized void startup() throws RhenaException, IOException, InterruptedException, NotBoundException {

		createRegistry();
		start();
	}

	private void createRegistry() throws RhenaException {

		/**
		 * Create registry
		 */
		try {
			final ServerSocket ss = new ServerSocket(0);
			// ss.close();
			rmiRegistryPort = ss.getLocalPort();

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

					return ss;
				}
			};

			registry = LocateRegistry.createRegistry(rmiRegistryPort, fact, fact);
			registry.rebind(ILifecycleAgentManager.class.getName(), (ILifecycleAgentManager) this);
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
		if (config.getAgentClasspath() != null) {
			cmd.add("-classpath");
			cmd.add(config.getAgentClasspath());
		}
		if (config.getProfilerClasspath() != null) {
			cmd.add("-javaagent:" + config.getProfilerClasspath() + "=\"" + rmiRegistryPort + "\"");
		}
		cmd.add(LifecycleAgent.class.getName());
		cmd.add(rmiRegistryPort + "");

		ProcessBuilder builder = new ProcessBuilder(cmd);

		logger.debug(getClass(), "Starting lifecycle agent: " + builder.command());
		// logger.info(getClass(), "Building process: " + builder.command());

		lifecycleAgentProcess = builder.inheritIO().start();
		lifecycleAgentProcessExitTracker = new ProcessExitTracker(lifecycleAgentProcess, config);
		lifecycleAgentProcessExitTracker.start();

		/**
		 * Sleep until the agent is ready
		 */
		wait(5000);

		if (!notifiedByAgent) {
			throw new RhenaException("Timeout reached, failed to start lifecycle agent");
		} else {
			for (IProcessListener listener : config.getAgentStartListeners()) {

				listener.onProcess(lifecycleAgentProcess);
			}
		}

		/**
		 * Now obtain the remoting interface
		 */
		Object remoteObj = registry.lookup(ILifecycleAgent.class.getName());
		logger.debug(getClass(), "Remote object is: " + remoteObj);
		this.lifecycleAgent = (ILifecycleAgent) registry.lookup(ILifecycleAgent.class.getName());
		logger.debug(getClass(), "Retrieved lifecycle agent in child process: " + lifecycleAgent);
	}

	@Override
	public synchronized void agentNotify() {

		this.notifiedByAgent = true;
		notifyAll();
	}

	@Override
	public ILifecycleAgent getLifecycleAgent() {

		return lifecycleAgent;
	}

	@Override
	public void export(String typeName, Remote object) throws AccessException, RemoteException, AlreadyBoundException {

		registry.bind(typeName, object);
	}
	
	@Override
	public IDiagnosticReport getAgentReport() throws AccessException, RemoteException, NotBoundException {
		
		IClassLoaderReporting clr = (IClassLoaderReporting) registry.lookup(IClassLoaderReporting.class.getName());
		return clr.produceRuntimeReport();
	}
}
