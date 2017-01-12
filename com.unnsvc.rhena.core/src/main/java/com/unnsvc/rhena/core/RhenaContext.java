
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.agent.LifecycleAgentManager;
import com.unnsvc.rhena.common.ILifecycleAgentBuilder;
import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.logging.ILoggerService;
import com.unnsvc.rhena.core.logging.LogFacade;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaContext implements IRhenaContext {

	private static final long serialVersionUID = 1L;
	private IRhenaConfiguration config;
	private IRhenaCache cache;
	private List<IRepository> repositories;
	private List<IRepository> additionalRepositories;
	private IRepository localCacheRepository;
	private IListenerConfiguration listenerConfig;
	private ILogger logFacade;
	private ILifecycleAgentBuilder lifecycleAgentManager;

	/**
	 * @throws RhenaException
	 * @TODO this remains from old code
	 */
	public RhenaContext(IRhenaConfiguration config) throws RhenaException {

		try {
			this.config = config;
			this.cache = new RhenaCache(this);
			this.repositories = new ArrayList<IRepository>();
			this.additionalRepositories = new ArrayList<IRepository>();
			this.listenerConfig = new ListenerConfiguration();
			this.logFacade = new LogFacade(listenerConfig);
			startupContext();
		} catch (Throwable t) {
			throw new RhenaException(t.getMessage(), t);
		}
	}

	private void startupContext() throws RhenaException {

		try {
			lifecycleAgentManager = new LifecycleAgentManager(config.getAgentClasspath(), config.getProfilerClasspath());
			lifecycleAgentManager.startup();
			/**
			 * @TODO export relevant objects
			 */
			lifecycleAgentManager.export(ILoggerService.class.getName(), (ILoggerService) logFacade);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
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

		return repositories;
	}

	public void addWorkspaceRepository(IRepository repository) {

		this.repositories.add(repository);
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
		getCache().getMerged().clear();
	}

	@Override
	public ILifecycleAgentBuilder getLifecycleAgentManager() throws RhenaException {

		return lifecycleAgentManager;
	}

}
