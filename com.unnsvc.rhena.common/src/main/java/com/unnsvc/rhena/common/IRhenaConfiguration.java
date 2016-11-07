package com.unnsvc.rhena.common;

import java.util.List;

public interface IRhenaConfiguration {

	public void setRunTest(boolean runTest);

	public void setRunItest(boolean runItest);

	public void setPackageWorkspace(boolean packageWorkspace);

	public void setInstallLocal(boolean installLocal);
	
	public void setParallel(boolean parallel);
	
	public boolean isParallel();

	public List<IRepository> getRepositories();

	public void addRepository(IRepository repository);

}
