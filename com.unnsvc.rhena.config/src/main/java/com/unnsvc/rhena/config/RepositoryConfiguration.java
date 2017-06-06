
package com.unnsvc.rhena.config;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public class RepositoryConfiguration implements IRepositoryConfiguration {

	private List<IRepositoryDefinition> workspaceRepositories;
	private List<IRepositoryDefinition> cacheRepositories;
	private List<IRepositoryDefinition> remoteRepositories;

	public RepositoryConfiguration() {

		this.workspaceRepositories = new ArrayList<IRepositoryDefinition>();
		this.remoteRepositories = new ArrayList<IRepositoryDefinition>();
		this.cacheRepositories = new ArrayList<IRepositoryDefinition>();
	}

	@Override
	public List<IRepositoryDefinition> getWorkspaceRepositories() {

		return workspaceRepositories;
	}

	@Override
	public void addWorkspaceRepositories(IRepositoryDefinition workspaceRepository) {

		this.workspaceRepositories.add(workspaceRepository);
	}

	public List<IRepositoryDefinition> getCacheRepositories() {

		return cacheRepositories;
	}

	@Override
	public void addCacheRepository(IRepositoryDefinition cacheRepository) {

		this.cacheRepositories.add(cacheRepository);
	}

	@Override
	public List<IRepositoryDefinition> getRemoteRepositories() {

		return remoteRepositories;
	}

	@Override
	public void addRemoteRepository(IRepositoryDefinition remoteRepository) {

		this.remoteRepositories.add(remoteRepository);
	}

	@Override
	public void addRepository(IRepositoryDefinition definition) {

		switch (definition.getRepositoryType()) {
			case CACHE:
				this.cacheRepositories.add(definition);
				break;
			case WORKSPACE:
				this.workspaceRepositories.add(definition);
				break;
			case REMOTE:
				this.remoteRepositories.add(definition);
				break;
		}
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheRepositories == null) ? 0 : cacheRepositories.hashCode());
		result = prime * result + ((remoteRepositories == null) ? 0 : remoteRepositories.hashCode());
		result = prime * result + ((workspaceRepositories == null) ? 0 : workspaceRepositories.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepositoryConfiguration other = (RepositoryConfiguration) obj;
		if (cacheRepositories == null) {
			if (other.cacheRepositories != null)
				return false;
		} else if (!cacheRepositories.equals(other.cacheRepositories))
			return false;
		if (remoteRepositories == null) {
			if (other.remoteRepositories != null)
				return false;
		} else if (!remoteRepositories.equals(other.remoteRepositories))
			return false;
		if (workspaceRepositories == null) {
			if (other.workspaceRepositories != null)
				return false;
		} else if (!workspaceRepositories.equals(other.workspaceRepositories))
			return false;
		return true;
	}

}
