package com.unnsvc.rhena.common.config;

import java.io.Serializable;

public interface IBuildConfiguration extends Serializable {

	public void setPackageWorkspace(boolean packageWorkspace);

	public boolean isPackageWorkspace();

	public void setParallel(boolean parallel);

	public boolean isParallel();

}
