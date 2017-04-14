
package com.unnsvc.rhena.common.ng.config;

import java.util.List;

import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public interface IRepositoryConfiguration {

	public List<IRepositoryDefinition> getWorkspaceRepositories();

	public void addWorkspaceRepositories(IRepositoryDefinition workspaceRepository);

	public IRepositoryDefinition getCacheRepository();

	public void setCacheRepository(IRepositoryDefinition cacheRepository);

	public List<IRepositoryDefinition> getRemoteRepositories();

	public void addRemoteRepository(IRepositoryDefinition remoteRepository);

}
