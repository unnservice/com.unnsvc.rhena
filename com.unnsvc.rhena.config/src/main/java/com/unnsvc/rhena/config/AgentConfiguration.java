
package com.unnsvc.rhena.config;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.config.IAgentConfiguration;

public class AgentConfiguration implements IAgentConfiguration {

	private int threads;
	private SocketAddress agentAddress;

	public AgentConfiguration() {

		this.threads = Runtime.getRuntime().availableProcessors();
	}

	@Override
	public void setAgentAddress(SocketAddress agentAddress) {

		this.agentAddress = agentAddress;
	}

	@Override
	public SocketAddress getAgentAddress() {

		return agentAddress;
	}

	@Override
	public void setThreads(int threads) {

		this.threads = threads;
	}

	@Override
	public int getThreads() {

		return threads;
	}
}
