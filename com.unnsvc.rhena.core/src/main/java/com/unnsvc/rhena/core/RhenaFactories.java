
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.agent.RhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.repository.IRepositoryFactory;
import com.unnsvc.rhena.repository.RepositoryFactory;

public class RhenaFactories implements IRhenaFactories {

	private IRhenaAgentClientFactory agentClientFactoy;
	private IRepositoryFactory repositoryFactory;

	public RhenaFactories() {

		this.agentClientFactoy = new RhenaAgentClientFactory();
		this.repositoryFactory = new RepositoryFactory();
	}

	@Override
	public IRhenaAgentClientFactory getAgentClientFactory() {

		return agentClientFactoy;
	}

	@Override
	public IRepositoryFactory getRepositoryFactory() {

		return repositoryFactory;
	}

}
