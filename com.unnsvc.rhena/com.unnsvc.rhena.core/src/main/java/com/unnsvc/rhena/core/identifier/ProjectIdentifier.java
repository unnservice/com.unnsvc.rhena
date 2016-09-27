package com.unnsvc.rhena.core.identifier;

public abstract class ProjectIdentifier extends QualifiedIdentifier {

	private Identifier component;
	private Identifier project;
	private Version version;

	ProjectIdentifier(Identifier component, Identifier project, Version version) {

		this.component = component;
		this.project = project;
		this.version = version;
	}

	public ProjectIdentifier(Identifier project, Version version) {
		
		this(null, project, version);
	}

	public Identifier getComponent() {
		return component;
	}

	public Identifier getProject() {
		return project;
	}

	public Version getVersion() {
		return version;
	}

	@Override
	public String toString() {

		return (component == null ? "" : component.toString() + ":") + project.toString() + ":" + version.toString();
	}
}
