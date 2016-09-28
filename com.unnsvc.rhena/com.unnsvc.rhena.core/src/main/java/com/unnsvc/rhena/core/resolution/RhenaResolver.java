package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.ComponentImportEdge;
import com.unnsvc.rhena.core.model.ProjectDependencyEdge;

public interface RhenaResolver {
	
	public ResolutionResult resolveComponent(ResolutionEngine engine, ComponentImportEdge componentImportEdge) throws RhenaException;

	public ResolutionResult resolveProject(ResolutionEngine engine, ProjectDependencyEdge projectDependencyEdge) throws RhenaException;

	public URI getLocation();
	
	public String getName();
}
