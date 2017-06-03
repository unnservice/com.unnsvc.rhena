
package com.unnsvc.rhena.config;

import com.unnsvc.rhena.common.config.IBuildConfiguration;

public class BuildConfiguration implements IBuildConfiguration {

	private boolean fullBuild;

	public BuildConfiguration() {

		this.fullBuild = false;
	}

	@Override
	public void setFullBuild(boolean fullBuild) {

		this.fullBuild = fullBuild;
	}

	@Override
	public boolean isFullBuild() {

		return fullBuild;
	}
}
