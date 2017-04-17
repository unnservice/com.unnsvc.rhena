
package com.unnsvc.rhena.config;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public class RhenaRepositoryConfiguration implements IRepositoryConfiguration {

	private List<IRepositoryDefinition> workspaceRepositories;
	private IRepositoryDefinition cacheRepository;
	private List<IRepositoryDefinition> remoteRepositories;

	public RhenaRepositoryConfiguration() {

		this.workspaceRepositories = new ArrayList<IRepositoryDefinition>();
		this.remoteRepositories = new ArrayList<IRepositoryDefinition>();
	}

	@Override
	public List<IRepositoryDefinition> getWorkspaceRepositories() {

		return workspaceRepositories;
	}

	@Override
	public void addWorkspaceRepositories(IRepositoryDefinition workspaceRepository) {

		this.workspaceRepositories.add(workspaceRepository);
	}

	@Override
	public IRepositoryDefinition getCacheRepository() {

		return cacheRepository;
	}

	@Override
	public void setCacheRepository(IRepositoryDefinition cacheRepository) {

		this.cacheRepository = cacheRepository;
	}

	@Override
	public List<IRepositoryDefinition> getRemoteRepositories() {

		return remoteRepositories;
	}

	@Override
	public void addRemoteRepository(IRepositoryDefinition remoteRepository) {

		this.remoteRepositories.add(remoteRepository);
	}

}
