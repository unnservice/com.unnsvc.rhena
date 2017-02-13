
package com.unnsvc.rhena.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.agent.LifecycleAgentManager;
import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.agent.ILifecycleAgentManager;
import com.unnsvc.rhena.common.config.IRepositoryDefinition;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.common.process.IProcessListener;
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
	private ILifecycleAgentManager lifecycleAgentManager;

	private List<IProcessListener> agentExitListeners;
	private List<IProcessListener> agentStartListeners;

	/**
	 * @throws RhenaException
	 * @TODO this remains from old code
	 */
	public RhenaContext(IRhenaConfiguration config) throws RhenaException {

		try {
			this.config = config;
			this.cache = new RhenaCache();
			this.workspaceRepositories = new ArrayList<IRepository>();
			this.additionalRepositories = new ArrayList<IRepository>();
			this.listenerConfig = new ListenerConfiguration();
			this.logFacade = new LogFacade(listenerConfig);
			this.agentExitListeners = new ArrayList<IProcessListener>();
			this.agentStartListeners = new ArrayList<IProcessListener>();
			this.localCacheRepository = new LocalCacheRepository(this, config);
			
			for(IRepositoryDefinition repoDef : config.getRepositoryConfiguration().getRepositories()) {
				
				addAdditionalRepository(new RemoteRepository(this, repoDef.getLocation()));
			}
			
			for(IRepositoryDefinition repoDef : config.getRepositoryConfiguration().getWorkspaces()) {

				addWorkspaceRepository(new WorkspaceRepository(this, new File(repoDef.getLocation())));
			}

			initialConfiguration();
			startupContext();
		} catch (Throwable t) {
			throw new RhenaException(t.getMessage(), t);
		}
	}

	/**
	 * Fill with initial configuration from the settings
	 */
	private void initialConfiguration() {

	}

	private void startupContext() throws RhenaException {

		try {
			lifecycleAgentManager = new LifecycleAgentManager(config, this);
			lifecycleAgentManager.startup();
			/**
			 * @TODO export relevant objects
			 */
			lifecycleAgentManager.export(ILoggerService.class.getName(), (ILoggerService) logFacade);
		} catch (Exception ex) {
			throw new RhenaException("Failed to start lifecycle agent", ex);
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

	@Override
	public List<IProcessListener> getAgentExitListeners() {

		return agentExitListeners;
	}

	@Override
	public List<IProcessListener> getAgentStartListeners() {

		return agentStartListeners;
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
	public ILifecycleAgentManager getLifecycleAgentManager() throws RhenaException {

		return lifecycleAgentManager;
	}
}
