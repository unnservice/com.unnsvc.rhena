
package com.unnsvc.rhena.core.config;

import com.unnsvc.rhena.common.config.IBuildConfiguration;

public class BuildConfiguration implements IBuildConfiguration {

	private static final long serialVersionUID = 1L;
	private boolean packageWorkspace;
	private boolean parallel;
	
	public BuildConfiguration() {
		
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
}
