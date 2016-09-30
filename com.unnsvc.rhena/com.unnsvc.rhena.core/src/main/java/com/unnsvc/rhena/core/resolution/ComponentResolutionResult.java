package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.model.RhenaComponent;

public class ComponentResolutionResult extends ResolutionResult {

	private RhenaComponent componentDesc;

	public ComponentResolutionResult(RhenaComponent componentDesc) {

		super("success?");
		this.componentDesc = componentDesc;
	}

	public RhenaComponent getComponent() {

		return componentDesc;
	}

}
