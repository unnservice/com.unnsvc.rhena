
package com.unnsvc.rhena.config;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;

/**
 * @TODO socket address is generated in constructor so it won't be serialized
 *       correctly on remotes
 * @author noname
 *
 */
public class RhenaConfiguration implements IRhenaConfiguration {

	private IRepositoryConfiguration repositoryConfiguration;
	private int threads;
	private SocketAddress agentAddress;
	private int agentTimeout;
	private boolean fullBuild;

	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
		this.threads = Runtime.getRuntime().availableProcessors();
		this.agentTimeout = 1000;
		this.fullBuild = false;
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

	@Override
	public int getAgentTimeout() {

		return agentTimeout;
	}

	@Override
	public void setAgentTimeout(int agentTimeout) {

		this.agentTimeout = agentTimeout;
	}
	
	@Override
	public boolean isFullBuild() {

		return fullBuild;
	}
}
