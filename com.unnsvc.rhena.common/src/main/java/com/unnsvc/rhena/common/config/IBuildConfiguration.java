package com.unnsvc.rhena.common.config;


public interface IBuildConfiguration {

	public void setPackageWorkspace(boolean packageWorkspace);

	public boolean isPackageWorkspace();

	public void setParallel(boolean parallel);

	public boolean isParallel();

}
