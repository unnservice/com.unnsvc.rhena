
package com.unnsvc.rhena.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.Version;

public class RhenaComponent implements RhenaNode {

	private ComponentIdentifier componentIdentifier;
	private List<RhenaComponentEdge> imports;
	private Set<RhenaProject> projects;
	private List<RhenaDependencyEdge> dependencies;
	private Version version;

	public RhenaComponent(String componentName) throws RhenaParserException {

		this.componentIdentifier = new ComponentIdentifier(componentName);
		this.imports = new ArrayList<RhenaComponentEdge>();
		this.projects = new HashSet<RhenaProject>();
		this.dependencies = new ArrayList<RhenaDependencyEdge>();
	}

	public RhenaProject getProject(String projectName) {

		for (RhenaProject project : projects) {
			if (project.getName().equals(projectName)) {
				return project;
			}
		}
		return null;
	}

	public void addImport(RhenaComponentEdge componentImportEdge) {

		this.imports.add(componentImportEdge);
	}

	public void addProject(RhenaProject project) {

		this.projects.add(project);
	}

	public ComponentIdentifier getComponentIdentifier() {

		return componentIdentifier;
	}

	public Version getVersion() {

		return version;
	}

	public void setVersion(Version version) {

		this.version = version;
	}

	public void addDependency(RhenaDependencyEdge dependency) {

		this.dependencies.add(dependency);
	}

}
