
package com.unnsvc.rhena.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.process.IProcessListener;

public class RhenaConfiguration implements IRhenaConfiguration {

	private static final long serialVersionUID = 1L;
	private File rhenaHome;
	private boolean runTest;
	private boolean runItest;
	private boolean packageWorkspace;
	private boolean installLocal;
	private boolean parallel;
	private String agentClasspath;
	private String profilerClasspath;
	private List<IProcessListener> agentExitListeners;
	private List<IProcessListener> agentStartListeners;

	public RhenaConfiguration() {

		this.agentExitListeners = new ArrayList<IProcessListener>();
		this.agentStartListeners = new ArrayList<IProcessListener>();
	}

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

	@Override
	public void setAgentClasspath(String agentClasspath) {

		this.agentClasspath = agentClasspath;
	}

	@Override
	public String getAgentClasspath() {

		return agentClasspath;
	}

	@Override
	public void setProfilerClasspath(String profilerClasspath) {

		this.profilerClasspath = profilerClasspath;
	}

	@Override
	public String getProfilerClasspath() {

		return profilerClasspath;
	}

	@Override
	public List<IProcessListener> getAgentExitListeners() {

		return agentExitListeners;
	}

	@Override
	public List<IProcessListener> getAgentStartListeners() {

		return agentStartListeners;
	}
}
