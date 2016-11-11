
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URL;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(File location) {

		super(location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IModelResolver resolver, IEntryPoint entryPoint) throws RhenaException {

		// lifecycle dependency chain
		// create dependency chains
		// produce classloader
		IRhenaModule module = resolver.materialiseModel(entryPoint.getTarget());

		
		
		try {

			return new RhenaExecution(module.getIdentifier(), entryPoint.getExecutionType(), new ArtifactDescriptor("someartifact", new URL("http://some.url.com/"), "not-implemented"));
		} catch (Exception e) {
			throw new RhenaException(e);
		}
	}

}
