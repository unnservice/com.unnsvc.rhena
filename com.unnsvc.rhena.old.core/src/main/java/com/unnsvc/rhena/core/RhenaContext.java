
package com.unnsvc.rhena.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.config.IRepositoryDefinition;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.core.logging.LogFacade;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.RemoteRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaContext implements IRhenaContext {

	private IRhenaConfiguration config;
	private IRhenaCache cache;
	private List<IRepository> workspaceRepositories;
	private List<IRepository> additionalRepositories;
	private IRepository localCacheRepository;
	private IListenerConfiguration listenerConfig;
	private ILogger logFacade;
	private IAgentClient agentClient;

	/**
	 * @throws RhenaException
	 * @TODO this remains from old code
	 */
	public RhenaContext(IRhenaConfiguration config, IAgentClient agentClient) throws RhenaException {

		try {

			this.config = config;
			this.agentClient = agentClient;
			this.cache = new RhenaCache();
			this.workspaceRepositories = new ArrayList<IRepository>();
			this.additionalRepositories = new ArrayList<IRepository>();
			this.listenerConfig = new ListenerConfiguration();
			this.logFacade = new LogFacade(listenerConfig);
			this.localCacheRepository = new LocalCacheRepository(this, config);

			initialConfiguration();
		} catch (Throwable t) {
			throw new RhenaException(t.getMessage(), t);
		}
	}

	/**
	 * Fill with initial configuration from the settings
	 */
	private void initialConfiguration() {

		for (IRepositoryDefinition repoDef : config.getRepositoryConfiguration().getRepositories()) {

			addAdditionalRepository(new RemoteRepository(this, repoDef.getLocation()));
		}

		for (IRepositoryDefinition repoDef : config.getRepositoryConfiguration().getWorkspaces()) {

			addWorkspaceRepository(new WorkspaceRepository(this, new File(repoDef.getLocation())));
		}
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}

	@Override
	public IRhenaCache getCache() {

		return cache;
	}

	@Override
	public IListenerConfiguration getListenerConfig() {

		return listenerConfig;
	}

	@Override
	public List<IRepository> getWorkspaceRepositories() {

		return workspaceRepositories;
	}

	public void addWorkspaceRepository(IRepository repository) {

		this.workspaceRepositories.add(repository);
	}

	@Override
	public ILogger getLogger() {

		return this.logFacade;
	}

	@Override
	public void setLocalRepository(IRepository localCacheRepository) {

		this.localCacheRepository = localCacheRepository;
	}

	@Override
	public IRepository getLocalCacheRepository() {

		return localCacheRepository;
	}

	@Override
	public List<IRepository> getAdditionalRepositories() {

		return additionalRepositories;
	}

	@Override
	public void addAdditionalRepository(IRepository repository) {

		this.additionalRepositories.add(repository);
	}

	/**
	 * @TODO clean caches and everything
	 */
	@Override
	public void close() throws Exception {

		getCache().getExecutions().clear();
		getCache().getLifecycles().clear();

		// Only remove lifecycles and executions

		getCache().getModules().clear();
		getCache().getEdges().clear();
	}

	@Override
	public void setAgent(IAgentClient agentClient) {

		this.agentClient = agentClient;
	}

	@Override
	public IAgentClient getAgent() {

		return agentClient;
	}
}
