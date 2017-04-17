
package com.unnsvc.rhena.config;

import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;

public class RhenaConfiguration implements IRhenaConfiguration {

	private IRepositoryConfiguration repositoryConfiguration;
	private int threads;

	public RhenaConfiguration() {

		this.repositoryConfiguration = new RhenaRepositoryConfiguration();
		this.threads = Runtime.getRuntime().availableProcessors();
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

}
