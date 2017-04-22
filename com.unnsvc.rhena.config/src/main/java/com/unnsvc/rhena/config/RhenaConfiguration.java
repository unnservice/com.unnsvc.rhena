
package com.unnsvc.rhena.config;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;

public class RhenaConfiguration implements IRhenaConfiguration {

	private IRepositoryConfiguration repositoryConfiguration;
	private int threads;
	private SocketAddress agentAddress;

	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
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

	@Override
	public IRepositoryConfiguration getRepositoryConfiguration() {

		return repositoryConfiguration;
	}

	@Override
	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration) {

		this.repositoryConfiguration = repositoryConfiguration;
	}
}
