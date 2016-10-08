
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class RhenaContext {

	private ResolutionManager resolutionManager;

	public RhenaContext(ResolutionManager resolutionManager) {

		this.resolutionManager = resolutionManager;
	}

	public ResolutionManager getResolutionManager() {

		return resolutionManager;
	}
}
