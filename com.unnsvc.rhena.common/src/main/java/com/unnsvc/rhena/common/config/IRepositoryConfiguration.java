
package com.unnsvc.rhena.common.config;

import java.util.List;

import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public interface IRepositoryConfiguration {

	public List<IRepositoryDefinition> getWorkspaceRepositories();

	public void addWorkspaceRepositories(IRepositoryDefinition workspaceRepository);

	public List<IRepositoryDefinition> getCacheRepositories();

	public void addCacheRepository(IRepositoryDefinition cacheRepository);

	public List<IRepositoryDefinition> getRemoteRepositories();

	public void addRemoteRepository(IRepositoryDefinition remoteRepository);

}
