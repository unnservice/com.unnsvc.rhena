
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.process.IProcessListener;

public interface IRhenaConfiguration extends Serializable {

	public void setRhenaHome(File rhenaHome);

	public File getRhenaHome();

	public void setRunTest(boolean runTest);

	public void setRunItest(boolean runItest);

	public void setPackageWorkspace(boolean packageWorkspace);

	public void setInstallLocal(boolean installLocal);

	public void setParallel(boolean parallel);

	public boolean isParallel();

	public boolean isRunTest();

	public boolean isRunItest();

	public boolean isPackageWorkspace();

	public boolean isInstallLocal();

	public String getAgentClasspath();

	public void setAgentClasspath(String agentClasspath);

	public String getProfilerClasspath();

	public void setProfilerClasspath(String profilerClasspath);

	public List<IProcessListener> getAgentExitListeners();

	public List<IProcessListener> getAgentStartListeners();
}
