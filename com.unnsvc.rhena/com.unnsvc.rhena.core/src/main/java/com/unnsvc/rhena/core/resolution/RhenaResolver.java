package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.model.RhenaComponent;
import com.unnsvc.rhena.core.model.RhenaProjectNode;

public interface RhenaResolver {
	
	public RhenaComponent resolveComponent(ComponentIdentifier identifier) throws RhenaException;

	public RhenaProjectNode resolveProject(ProjectIdentifier identifier) throws RhenaException;

	public URI getLocation();
	
	public String getName();
}
