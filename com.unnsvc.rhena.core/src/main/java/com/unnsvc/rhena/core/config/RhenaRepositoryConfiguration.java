
package com.unnsvc.rhena.core.config;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.repository.IRepository;

public class RhenaRepositoryConfiguration implements IRepositoryConfiguration {

	private List<IRepository> workspaceRepositories;
	private IRepository cacheRepository;
	private List<IRepository> remoteRepositories;

	public RhenaRepositoryConfiguration() {

		this.workspaceRepositories = new ArrayList<IRepository>();
	}

	@Override
	public List<IRepository> getWorkspaceRepositories() {

		return workspaceRepositories;
	}

	@Override
	public void addWorkspaceRepositories(IRepository workspaceRepository) {

		this.workspaceRepositories.add(workspaceRepository);
	}

	@Override
	public IRepository getCacheRepository() {

		return cacheRepository;
	}

	@Override
	public void setCacheRepository(IRepository cacheRepository) {

		this.cacheRepository = cacheRepository;
	}

	@Override
	public List<IRepository> getRemoteRepositories() {

		return remoteRepositories;
	}

	@Override
	public void addRemoteRepository(IRepository remoteRepository) {

		this.remoteRepositories.add(remoteRepository);
	}

}
