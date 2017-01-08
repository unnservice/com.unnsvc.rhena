
package com.unnsvc.rhena.common;

import java.util.List;

import com.unnsvc.rhena.common.logging.ILogger;

public interface IRhenaContext extends AutoCloseable {

	public IRhenaConfiguration getConfig();

	public List<IRepository> getWorkspaceRepositories();

	public void addWorkspaceRepository(IRepository repository);

	public ILogger getLogger();

	public void setLocalRepository(IRepository localCacheRepository);

	public IRepository getLocalCacheRepository();

	public IListenerConfiguration getListenerConfig();

}
