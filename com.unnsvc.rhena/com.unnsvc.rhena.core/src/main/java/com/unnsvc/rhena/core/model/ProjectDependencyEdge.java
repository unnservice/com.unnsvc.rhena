package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.identifier.Version;

public class ProjectDependencyEdge implements RhenaNodeEdge {

	private ProjectIdentifier projectIdentifier;
	private String resolver;
	private String scope;

	public ProjectDependencyEdge(String componentName, String scope,  String projectName, String version) throws RhenaParserException {

		this.projectIdentifier = ProjectIdentifier.valueOfProject(componentName + ":" + projectName + ":" + version);
		this.resolver = Constants.SCOPE_DEFAULT;
		this.scope = scope;
	}
	
	public ProjectDependencyEdge(String componentName,String scope, String projectName, String version, String resolver) throws RhenaParserException {

		this.projectIdentifier = ProjectIdentifier.valueOfProject(componentName + ":" + projectName + ":" + version);
		this.resolver = resolver;
		this.scope = scope;
	}

	public String getComponentName() {

		return projectIdentifier.getComponent().toString();
	}

	public String getProjectName() {

		return projectIdentifier.getProject().toString();
	}

	public Version getVersion() {

		return projectIdentifier.getVersion();
	}

	@Override
	public String toString() {
		return "ProjectDependencyEdge [projectIdentifier=" + projectIdentifier + "]";
	}

	@Override
	public String getResolverName() {
		
		return resolver;
	}

}
