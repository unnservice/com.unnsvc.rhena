
package com.unnsvc.rhena.config;

import com.unnsvc.rhena.common.config.IAgentConfiguration;
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
	private IAgentConfiguration agentConfiguration;

	private boolean fullBuild;

	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
		this.agentConfiguration = new AgentConfiguration();

		this.fullBuild = false;
	}

	@Override
	public IAgentConfiguration getAgentConfiguration() {

		return agentConfiguration;
	}

	@Override
	public void setAgentConfiguration(IAgentConfiguration agentConfiguration) {

		this.agentConfiguration = agentConfiguration;
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
	public boolean isFullBuild() {

		return fullBuild;
	}
}
