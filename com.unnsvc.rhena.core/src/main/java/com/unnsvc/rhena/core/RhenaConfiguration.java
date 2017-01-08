
package com.unnsvc.rhena.core;

import java.io.File;

import com.unnsvc.rhena.common.IRhenaConfiguration;

public class RhenaConfiguration implements IRhenaConfiguration {

	private File rhenaHome;
	private boolean runTest;
	private boolean runItest;
	private boolean packageWorkspace;
	private boolean installLocal;
	private boolean parallel;

	@Override
	public void setRhenaHome(File rhenaHome) {

		this.rhenaHome = rhenaHome;
	}

	@Override
	public File getRhenaHome() {

		return rhenaHome;
	}

	@Override
	public void setRunTest(boolean runTest) {

		this.runTest = runTest;
	}

	@Override
	public boolean isRunTest() {

		return runTest;
	}

	@Override
	public void setRunItest(boolean runItest) {

		this.runItest = runItest;
	}

	@Override
	public boolean isRunItest() {

		return runItest;
	}

	@Override
	public void setPackageWorkspace(boolean packageWorkspace) {

		this.packageWorkspace = packageWorkspace;
	}

	@Override
	public boolean isPackageWorkspace() {

		return packageWorkspace;
	}

	@Override
	public void setInstallLocal(boolean installLocal) {

		this.installLocal = installLocal;
	}

	@Override
	public boolean isInstallLocal() {

		return installLocal;
	}

	@Override
	public void setParallel(boolean parallel) {

		this.parallel = parallel;
	}

	@Override
	public boolean isParallel() {

		return parallel;
	}
}
