
package com.unnsvc.rhena.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.core.logging.LogFacade;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaContext implements IRhenaContext {

	private boolean runTest;
	private boolean runItest;
	private boolean packageWorkspace;
	private boolean installLocal;
	private boolean parallel;
	private List<IRepository> repositories;
	private IRepository localCacheRepository;
	private File rhenaHome;
	private IListenerConfiguration listenerConfig;
	private LogFacade logFacade;

	/**
	 * @TODO this remains from old code
	 */
	public RhenaContext() {

		this.repositories = new ArrayList<IRepository>();
		this.listenerConfig = new ListenerConfiguration();
		this.logFacade = new LogFacade(listenerConfig);
	}

	@Override
	public IListenerConfiguration getListenerConfig() {

		return listenerConfig;
	}

	@Override
	public void setRunTest(boolean runTest) {

		this.runTest = runTest;
	}

	public boolean isRunTest() {

		return runTest;
	}

	@Override
	public void setRunItest(boolean runItest) {

		this.runItest = runItest;
	}

	public boolean isRunItest() {

		return runItest;
	}

	@Override
	public void setPackageWorkspace(boolean packageWorkspace) {

		this.packageWorkspace = packageWorkspace;
	}

	public boolean isPackageWorkspace() {

		return packageWorkspace;
	}

	@Override
	public void setInstallLocal(boolean installLocal) {

		this.installLocal = installLocal;
	}

	public boolean isInstallLocal() {

		return installLocal;
	}

	@Override
	public List<IRepository> getWorkspaceRepositories() {

		return repositories;
	}

	public void addWorkspaceRepository(IRepository repository) {

		this.repositories.add(repository);
	}

	@Override
	public void setParallel(boolean parallel) {

		this.parallel = parallel;
	}

	@Override
	public boolean isParallel() {

		return parallel;
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
	public void setRhenaHome(File rhenaHome) {

		this.rhenaHome = rhenaHome;
	}

	@Override
	public File getRhenaHome() {

		return rhenaHome;
	}
}
