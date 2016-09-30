
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.RhenaComponentEdge;
import com.unnsvc.rhena.core.model.RhenaDependencyEdge;

public class MavenResolver implements RhenaResolver {

	@Override
	public ResolutionResult resolveComponent(ResolutionEngine engine, RhenaComponentEdge componentImportEdge) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ResolutionResult resolveProject(ResolutionEngine engine, RhenaDependencyEdge projectDependencyEdge) throws RhenaException {

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
