package com.unnsvc.rhena.core.model;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.core.identifier.Version;

public class RhenaComponentDescriptor implements RhenaNode {

	private String componentName;
	private Set<ComponentImportEdge> imports;
	private Set<RhenaProject> projects;
	private Version version;

	public RhenaComponentDescriptor(String componentName) {

		this.componentName = componentName;
		this.imports = new HashSet<ComponentImportEdge>();
		this.projects = new HashSet<RhenaProject>();
	}

	public RhenaProject getProject(String projectName) {

		for (RhenaProject project : projects) {
			if (project.getName().equals(projectName)) {
				return project;
			}
		}
		return null;
	}

	public void addImport(ComponentImportEdge componentImportEdge) {

		this.imports.add(componentImportEdge);
	}

	public void addProject(RhenaProject project) {

		this.projects.add(project);
	}

	public Version getVersion() {

		return version;
	}

	public void setVersion(Version version) {

		this.version = version;
	}

}
