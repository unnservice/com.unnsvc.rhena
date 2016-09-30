
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.identifier.ProjectIdentifier;

public class RhenaDependencyEdge implements RhenaEdge<ProjectIdentifier> {

	private Scope scope;
	private ProjectIdentifier identifier;
	private RhenaProjectNode project;

	public RhenaDependencyEdge(Scope scope, ProjectIdentifier identifier) {

		this.scope = scope;
		this.identifier = identifier;
	}

	public Scope getScope() {

		return scope;
	}

	@Override
	public ProjectIdentifier getIdentifier() {

		return identifier;
	}

	public RhenaProjectNode getProject() {

		return project;
	}

	public void setProject(RhenaProject project) {

		this.project = project;
	}
}
