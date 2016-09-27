package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;

public interface IResolver {
	
	public ResolutionResult resolveComponent(ResolutionContext context, ComponentIdentifier componentIdentifier) throws RhenaException;

	public ResolutionResult resolveProject(ResolutionContext context, ProjectIdentifier projectIdentifier) throws RhenaException;

	public URI getLocation();
}
