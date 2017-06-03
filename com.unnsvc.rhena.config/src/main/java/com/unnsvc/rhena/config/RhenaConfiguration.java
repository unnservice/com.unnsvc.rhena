
package com.unnsvc.rhena.config;

import com.unnsvc.rhena.common.config.IAgentConfiguration;
import com.unnsvc.rhena.common.config.IBuildConfiguration;
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
	private IBuildConfiguration buildConfiguration;


	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
		this.agentConfiguration = new AgentConfiguration();
		this.buildConfiguration = new BuildConfiguration();
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
	public IBuildConfiguration getBuildConfiguration() {

		return buildConfiguration;
	}

	@Override
	public void setBuildConfiguration(IBuildConfiguration buildConfiguration) {

		this.buildConfiguration = buildConfiguration;
	}

}
