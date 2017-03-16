
package com.unnsvc.rhena.core.config;

import java.io.File;

import com.unnsvc.rhena.common.config.IBuildConfiguration;
import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaConfiguration implements IRhenaConfiguration {

	private static final long serialVersionUID = 1L;
	private File instanceHome;
	private IRepositoryConfiguration repositoryConfiguration;
	private IBuildConfiguration buildConfiguration;

	public RhenaConfiguration() throws RhenaException {

		this.instanceHome = new File(System.getProperty("user.home"), ".rhena");
		this.repositoryConfiguration = new RepositoryConfiguration();
		this.buildConfiguration = new BuildConfiguration();
	}

	@Override
	public IRepositoryConfiguration getRepositoryConfiguration() {

		return repositoryConfiguration;
	}

	@Override
	public IBuildConfiguration getBuildConfiguration() {

		return buildConfiguration;
	}

	/**
	 * Used for example in local cache repository
	 */
	@Override
	public void setInstanceHome(File instanceHome) {

		this.instanceHome = instanceHome;
	}

	@Override
	public File getInstanceHome() {

		return instanceHome;
	}
}
