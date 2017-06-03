
package com.unnsvc.rhena.common.config;

public interface IRhenaConfiguration {

	public IRepositoryConfiguration getRepositoryConfiguration();

	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration);

	public IAgentConfiguration getAgentConfiguration();

	public void setAgentConfiguration(IAgentConfiguration agentConfiguration);

	/**
	 * Full build implies that the highest execution of the project is produced
	 * instead of lowest necessary (for example MAIN because it is required by
	 * model instead of ITEST)
	 * 
	 * @return
	 */
	public boolean isFullBuild();
}
