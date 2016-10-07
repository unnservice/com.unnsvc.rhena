
package com.unnsvc.rhena.ng;

import com.unnsvc.rhena.ng.resolution.ResolutionManager;

public class RhenaContext {

	private ResolutionManager resolutionManager;

	public RhenaContext(ResolutionManager resolutionManager) {

		this.resolutionManager = resolutionManager;
	}

	public ResolutionManager getResolutionManager() {

		return resolutionManager;
	}
}
