package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.ComponentImportEdge;
import com.unnsvc.rhena.core.model.ProjectDependencyEdge;

public class MavenResolver implements IResolver {

	@Override
	public ResolutionResult resolveComponent(ResolutionEngine engine, ComponentImportEdge componentImportEdge)
			throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ResolutionResult resolveProject(ResolutionEngine engine, ProjectDependencyEdge projectDependencyEdge)
			throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public URI getLocation() {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getName() {

		return "maven";
	}

	@Override
	public String toString() {
		return "MavenResolver [getName()=" + getName() + ", getLocation()=" + getLocation() + "]";
	}

}
