
package com.unnsvc.rhena.common;

import java.util.List;

import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.logging.ILogger;

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

	public void setAgent(IAgentClient agent);
	
	public IAgentClient getAgent();
}
