
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.agent.server.RhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.execution.IBuilderFactory;
import com.unnsvc.rhena.common.repository.IRepositoryFactory;
import com.unnsvc.rhena.execution.BuilderFactory;
import com.unnsvc.rhena.repository.RepositoryFactory;

public class RhenaFactories implements IRhenaFactories {

	private IRhenaAgentClientFactory agentClientFactoy;
	private IRepositoryFactory repositoryFactory;
	private IBuilderFactory builderFactory;

	public RhenaFactories() {

		this.agentClientFactoy = new RhenaAgentClientFactory();
		this.repositoryFactory = new RepositoryFactory();
		this.builderFactory = new BuilderFactory();
	}

	@Override
	public IRhenaAgentClientFactory getAgentClientFactory() {

		return agentClientFactoy;
	}

	@Override
	public IRepositoryFactory getRepositoryFactory() {

		return repositoryFactory;
	}

	@Override
	public IBuilderFactory getBuilderFactory() {

		return builderFactory;
	}

}
