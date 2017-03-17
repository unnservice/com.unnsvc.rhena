
package com.unnsvc.rhena.core.config;

import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.config.IAgentConfiguration;

public class AgentConfiguration implements IAgentConfiguration {

	private static final long serialVersionUID = 1L;
	private int agentPort;
	private String agentClasspath;
	private boolean externalAgent;

	public AgentConfiguration() {

		this.agentPort = AgentServerProcess.AGENT_EXECUTION_PORT;
		this.agentClasspath = System.getProperty("java.class.path");
		this.externalAgent = false;
	}

	@Override
	public void setAgentPort(int agentPort) {

		this.agentPort = agentPort;
	}

	@Override
	public int getAgentPort() {

		return agentPort;
	}

	@Override
	public void setAgentClasspath(String agentClasspath) {

		this.agentClasspath = agentClasspath;
	}

	@Override
	public String getAgentClasspath() {

		return agentClasspath;
	}

	@Override
	public void setExternalAgent(boolean externalAgent) {

		this.externalAgent = externalAgent;
	}

	@Override
	public boolean isExternalAgent() {

		return externalAgent;
	}
}
