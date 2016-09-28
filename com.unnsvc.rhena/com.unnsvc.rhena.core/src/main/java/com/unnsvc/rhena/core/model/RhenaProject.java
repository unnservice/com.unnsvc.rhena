package com.unnsvc.rhena.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.core.identifier.Version;

public class RhenaProject implements RhenaNode {

	private RhenaComponentDescriptor componentDescriptor;
	private String projectType;
	private String name;
	private Version version;
	private Properties properties;
	
	private List<ProjectDependencyEdge> dependencies;

	public RhenaProject(RhenaComponentDescriptor componentDescriptor, String projectType, String projectName) {

		this.componentDescriptor = componentDescriptor;
		this.projectType = projectType;
		this.name = projectName;
		this.dependencies = new ArrayList<ProjectDependencyEdge>();
		this.properties = new Properties();
	}

	public String getProjectType() {
		
		return projectType;
	}

	public String getName() {

		return name;
	}

	public Version getVersion() {

		if (this.version != null) {

			return version;
		}
		return componentDescriptor.getVersion();
	}

	public void addDependency(ProjectDependencyEdge dependency) {

		this.dependencies.add(dependency);
	}

	public void setProperties(Properties properties) {

		this.properties = properties;
	}

}
