package com.unnsvc.rhena.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.unnsvc.rhena.core.identifier.Version;

public class RhenaComponentDescriptor implements RhenaNode {

	private String componentName;
	private List<ComponentImportEdge> imports;
	private Set<RhenaProject> projects;
	private List<ProjectDependencyEdge> dependencies;
	private Version version;

	public RhenaComponentDescriptor(String componentName) {

		this.componentName = componentName;
		this.imports = new ArrayList<ComponentImportEdge>();
		this.projects = new HashSet<RhenaProject>();
		this.dependencies = new ArrayList<ProjectDependencyEdge>();
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

	public String getComponentName() {
		return componentName;
	}

	public Version getVersion() {

		return version;
	}

	public void setVersion(Version version) {

		this.version = version;
	}

	public void addDependency(ProjectDependencyEdge dependency) {

		this.dependencies.add(dependency);
	}

}
