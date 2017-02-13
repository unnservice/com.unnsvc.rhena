
package com.unnsvc.rhena.common;

import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleAgentManager;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.process.IProcessListener;

public interface IRhenaContext extends AutoCloseable {

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

	public List<IProcessListener> getAgentExitListeners();

	public List<IProcessListener> getAgentStartListeners();

	public ILifecycleAgentManager getLifecycleAgentManager() throws RhenaException;

}
