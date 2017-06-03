
package com.unnsvc.rhena.common.config;

public interface IRhenaConfiguration {

	public IRepositoryConfiguration getRepositoryConfiguration();

	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration);

	public IAgentConfiguration getAgentConfiguration();

	public void setAgentConfiguration(IAgentConfiguration agentConfiguration);

	public IBuildConfiguration getBuildConfiguration();

	public void setBuildConfiguration(IBuildConfiguration buildConfiguration);
}
