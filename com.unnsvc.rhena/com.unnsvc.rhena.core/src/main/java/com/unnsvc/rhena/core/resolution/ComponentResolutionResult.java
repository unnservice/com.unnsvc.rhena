package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.model.RhenaComponentDescriptor;

public class ComponentResolutionResult extends ResolutionResult {

	private RhenaComponentDescriptor componentDesc;

	public ComponentResolutionResult(RhenaComponentDescriptor componentDesc) {

		super(Status.SUCCESS);
		this.componentDesc = componentDesc;
	}

	public RhenaComponentDescriptor getComponent() {

		return componentDesc;
	}

}
