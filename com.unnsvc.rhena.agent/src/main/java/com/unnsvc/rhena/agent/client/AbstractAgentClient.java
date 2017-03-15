package com.unnsvc.rhena.agent.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.agent.IAgentClient;

public abstract class AbstractAgentClient implements IAgentClient {

	private Process agentProcess;
	private AgentClientControlThread controlThread;
	
	public void startup() throws IOException {

		startup(AgentServerProcess.AGENT_CONTROL_PORT);
	}

	public void startup(int port) throws IOException {

		String javaExecutable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

		List<String> cmd = new ArrayList<String>();
		cmd.add(javaExecutable);
		cmd.add("-classpath");
		cmd.add(System.getProperty("java.class.path"));
		cmd.add(AgentServerProcess.class.getName());
		cmd.add(port + "");

		ProcessBuilder builder = new ProcessBuilder(cmd);
		this.agentProcess = builder.inheritIO().start();

		controlThread = new AgentClientControlThread(AgentServerProcess.AGENT_CONTROL_PORT);
		controlThread.start();
	}

	public void shutdown() throws InterruptedException {

		System.out.println("client: Destroying agent server process");
		agentProcess.destroy();
		int exit = agentProcess.waitFor();
		System.out.println("client: Agent server process exit " + exit);
	}
}
