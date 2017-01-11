
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.Serializable;

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
}
