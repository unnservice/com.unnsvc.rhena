
package com.unnsvc.rhena.common;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleAgentManager;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;

public interface IRhenaContext extends AutoCloseable, Serializable {

	public IRhenaConfiguration getConfig();

	public IRhenaCache getCache();

	public List<IRepository> getWorkspaceRepositories();

	public void addWorkspaceRepository(IRepository repository);

	public ILogger getLogger();

	public void setLocalRepository(IRepository localCacheRepository);

	public IRepository getLocalCacheRepository();

	public IListenerConfiguration getListenerConfig();

	public List<IRepository> getAdditionalRepositories();

	public void addAdditionalRepository(IRepository repository);

	public ILifecycleAgentManager getLifecycleAgentManager() throws RhenaException;

}
