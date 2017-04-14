
package com.unnsvc.rhena.config;

import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;

public class RhenaConfiguration implements IRhenaConfiguration {

	private IRepositoryConfiguration repositoryConfiguration;

	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
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
