package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.Version;

public class ProjectDependencyEdge implements RhenaNodeEdge {

	private String componentName;
	private String projectName;
	private Version version;
	private String resolver;
	private String scope;

	public ProjectDependencyEdge(String componentName, String scope, String projectName, String version)
			throws RhenaParserException {

		this(componentName, scope, projectName, version, Constants.DEFAULT_RESOLVER);
	}

	public ProjectDependencyEdge(String componentName, String scope, String projectName, String versionString,
			String resolver) throws RhenaParserException {

		this.componentName = componentName;
		this.projectName = projectName;
		this.resolver = resolver;
		this.scope = scope;
		this.version = Version.valueOf(versionString);
	}

	public String getComponentName() {

		return componentName;
	}

	public String getProjectName() {

		return projectName;
	}

	public Version getVersion() {

		return version;
	}

	@Override
	public String getResolverName() {

		return resolver;
	}

	@Override
	public String toString() {
		return "ProjectDependencyEdge [componentName=" + componentName + ", projectName=" + projectName + ", version="
				+ version + ", resolver=" + resolver + ", scope=" + scope + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectDependencyEdge other = (ProjectDependencyEdge) obj;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
