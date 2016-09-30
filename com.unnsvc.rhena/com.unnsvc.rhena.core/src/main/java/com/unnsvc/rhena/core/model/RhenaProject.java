
package com.unnsvc.rhena.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.core.identifier.Version;

public class RhenaProject implements RhenaProjectNode {

	private RhenaComponent componentDescriptor;
	private String projectType;
	private String name;
	private Version version;
	private Properties properties;

	private List<RhenaDependencyEdge> dependencies;

	public RhenaProject(RhenaComponent componentDescriptor, String projectType, String projectName) {

		this.componentDescriptor = componentDescriptor;
		this.projectType = projectType;
		this.name = projectName;
		this.dependencies = new ArrayList<RhenaDependencyEdge>();
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

	public void addDependency(RhenaDependencyEdge dependency) {

		this.dependencies.add(dependency);
	}

	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	public List<RhenaDependencyEdge> getDependencies() {

		return dependencies;
	}

}
