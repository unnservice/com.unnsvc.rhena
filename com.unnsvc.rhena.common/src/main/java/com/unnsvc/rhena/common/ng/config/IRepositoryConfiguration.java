
package com.unnsvc.rhena.common.ng.config;

import java.util.List;

import com.unnsvc.rhena.common.ng.repository.IRepository;

public interface IRepositoryConfiguration {

	public List<IRepository> getWorkspaceRepositories();

	public void addWorkspaceRepositories(IRepository workspaceRepository);

	public IRepository getCacheRepository();

	public void setCacheRepository(IRepository cacheRepository);

	public List<IRepository> getRemoteRepositories();

	public void addRemoteRepository(IRepository remoteRepository);

}
