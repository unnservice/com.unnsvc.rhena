
package com.unnsvc.rhena.common.config;

import java.net.SocketAddress;

public interface IRhenaConfiguration {

	public IRepositoryConfiguration getRepositoryConfiguration();

	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration);

	public void setThreads(int threads);

	public int getThreads();

	public void setAgentAddress(SocketAddress agentAddress);

	public SocketAddress getAgentAddress();

	public void setAgentTimeout(int agentTimeout);

	public int getAgentTimeout();

	/**
	 * Full build implies that the highest execution of the project is produced
	 * instead of lowest necessary (for example MAIN because it is required by
	 * model instead of ITEST)
	 * 
	 * @return
	 */
	public boolean isFullBuild();
}
