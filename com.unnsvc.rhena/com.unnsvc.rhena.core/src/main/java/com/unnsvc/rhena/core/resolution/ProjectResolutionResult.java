package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.model.RhenaProject;

public class ProjectResolutionResult extends ResolutionResult {

	private RhenaProject project;

	public ProjectResolutionResult(RhenaProject project) {

		super(Status.SUCCESS);
		this.project = project;
	}

	public RhenaProject getProject() {
		
		return project;
	}
}
