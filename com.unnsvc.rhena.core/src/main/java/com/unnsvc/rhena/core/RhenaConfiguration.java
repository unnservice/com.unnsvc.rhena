
package com.unnsvc.rhena.core;

import java.io.File;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.settings.IRhenaSettings;
import com.unnsvc.rhena.core.settings.RhenaSettings;
import com.unnsvc.rhena.core.settings.RhenaSettingsParser;

public class RhenaConfiguration implements IRhenaConfiguration {

	private static final long serialVersionUID = 1L;
	private File rhenaHome;
	private boolean packageWorkspace;
	private boolean parallel;
	private String agentClasspath;
	private String profilerClasspath;
	private IRhenaSettings settings;

	public RhenaConfiguration() throws RhenaException {

		this.settings = new RhenaSettings();
	}

	public RhenaConfiguration(IRhenaSettings settings) throws RhenaException {

		this.settings = settings;
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
	public void setPackageWorkspace(boolean packageWorkspace) {

		this.packageWorkspace = packageWorkspace;
	}

	@Override
	public boolean isPackageWorkspace() {

		return packageWorkspace;
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
	public IRhenaSettings getSettings() {

		return settings;
	}
}
