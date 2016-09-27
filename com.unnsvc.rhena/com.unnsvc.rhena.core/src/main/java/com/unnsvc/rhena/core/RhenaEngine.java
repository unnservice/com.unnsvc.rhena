package com.unnsvc.rhena.core;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.IResolver;

public class RhenaEngine {

	private IResolver[] resolvers;

	public RhenaEngine(IResolver... resolvers) {

		this.resolvers = resolvers;
	}

	public RhenaProject createModel(String componentName, String projectName, String version) throws ResolverException {

		for (IResolver resolver : resolvers) {

			try {
				
				return resolver.resolveProject(componentName, projectName);
			} catch (Exception ex) {
				
				System.err.println("Not resolved in " + resolver.getClass());
			}
		}

		throw new ResolverException("Failed to resolve: " + componentName + ":" + projectName);
	}
}
