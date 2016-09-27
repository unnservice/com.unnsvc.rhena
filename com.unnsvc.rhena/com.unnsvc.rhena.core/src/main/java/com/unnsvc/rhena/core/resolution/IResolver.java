package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.model.RhenaProject;

public interface IResolver {

	public RhenaProject resolveProject(String componentName, String projectName) throws ResolverException;

}
