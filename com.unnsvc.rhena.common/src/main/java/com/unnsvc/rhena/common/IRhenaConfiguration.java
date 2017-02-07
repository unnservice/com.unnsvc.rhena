
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.Serializable;

import com.unnsvc.rhena.common.settings.IRhenaSettings;

public interface IRhenaConfiguration extends Serializable {

	public void setRhenaHome(File rhenaHome);

	public File getRhenaHome();

	public void setPackageWorkspace(boolean packageWorkspace);

	public void setParallel(boolean parallel);

	public boolean isParallel();

	public boolean isPackageWorkspace();

	public String getAgentClasspath();

	public void setAgentClasspath(String agentClasspath);

	public String getProfilerClasspath();

	public void setProfilerClasspath(String profilerClasspath);

	public IRhenaSettings getSettings();
}
