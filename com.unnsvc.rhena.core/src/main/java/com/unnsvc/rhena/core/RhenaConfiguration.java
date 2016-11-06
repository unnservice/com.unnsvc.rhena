
package com.unnsvc.rhena.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaConfiguration implements IRhenaConfiguration {

	private boolean runTest;
	private boolean runItest;
	private boolean packageWorkspace;
	private boolean installLocal;
	private List<IRepository> repositories;
	private IRhenaLoggingHandler logHandler;
	
	
	public RhenaConfiguration(IRhenaLoggingHandler logHandler) {
	
		this.logHandler = logHandler;
	}

	/**
	 * @TODO this remains from old code
	 */
	public RhenaConfiguration() {
		
		this.repositories = new ArrayList<IRepository>();
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
	public List<IRepository> getRepositories() {

		return repositories;
	}

	public void addRepository(IRepository repository) {

		this.repositories.add(repository);
	}
}
