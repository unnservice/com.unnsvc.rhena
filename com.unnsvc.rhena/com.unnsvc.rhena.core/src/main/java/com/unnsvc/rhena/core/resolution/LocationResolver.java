package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.model.RhenaProject;

public class LocationResolver implements IResolver {

	public LocationResolver(URI location) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RhenaProject resolveProject(String componentName, String projectName) throws ResolverException {

		throw new UnsupportedOperationException("Not implemented");
	}

}
