
package com.unnsvc.rhena.agent.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.process.IProcessListener;
import com.unnsvc.rhena.common.process.ProcessExitTracker;

public abstract class AbstractAgentClient implements IAgentClient {

	private Process agentProcess;
	private ProcessExitTracker agentProcessTracker;
	private List<IProcessListener> agentProcessListeners;

	private int controlPort;
	private String classpath;

	public AbstractAgentClient(int controlPort, String classpath, List<IProcessListener> agentProcessListeners) {

		this.controlPort = controlPort;
		this.classpath = classpath;
		this.agentProcessListeners = agentProcessListeners;
	}

	@Override
	public void startup() throws RhenaException {

		try {
			String javaExecutable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

			List<String> cmd = new ArrayList<String>();
			cmd.add(javaExecutable);
			cmd.add("-classpath");
			cmd.add(classpath);
			cmd.add(AgentServerProcess.class.getName());
			cmd.add(controlPort + "");

			ProcessBuilder builder = new ProcessBuilder(cmd);
			this.agentProcess = builder.inheritIO().start();

			agentProcessTracker = new ProcessExitTracker(agentProcess, agentProcessListeners);
			agentProcessTracker.start();
		} catch (IOException ioe) {
			throw new RhenaException(ioe);
		}
	}

	@Override
	public void shutdown() throws RhenaException {

		try {
			System.out.println("client: Destroying agent server process");
			agentProcess.destroy();
			int exit = agentProcess.waitFor();
			System.out.println("client: Agent server process exit " + exit);
		} catch (InterruptedException ie) {
			throw new RhenaException(ie);
		}
	}
}
