
package com.unnsvc.rhena.common.config;

import java.net.SocketAddress;

public interface IRhenaConfiguration {

	public IRepositoryConfiguration getRepositoryConfiguration();

	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration);

	public void setThreads(int threads);

	public int getThreads();

	public void setAgentAddress(SocketAddress agentAddress);

	public SocketAddress getAgentAddress();
}
