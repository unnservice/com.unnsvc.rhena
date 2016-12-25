
package com.unnsvc.rhena.common;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.logging.ILogAdapter;

public interface IRhenaConfiguration {

	public void setRunTest(boolean runTest);

	public void setRunItest(boolean runItest);

	public void setPackageWorkspace(boolean packageWorkspace);

	public void setInstallLocal(boolean installLocal);

	public void setParallel(boolean parallel);

	public boolean isParallel();

	public List<IRepository> getWorkspaceRepositories();

	public void addWorkspaceRepository(IRepository repository);

	public ILogAdapter getLogger(Class<?> clazz);

	public void setLocalRepository(IRepository localCacheRepository);

	public IRepository getLocalCacheRepository();

	public void setRhenaHome(File rhenaHome);

	public File getRhenaHome();
}
