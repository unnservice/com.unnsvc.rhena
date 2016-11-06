
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;
import java.net.URL;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class WorkspaceRepository implements IRepository {

	private File location;

	public WorkspaceRepository(File location) {

		this.location = location.getAbsoluteFile();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		File moduleDirectory = new File(location, identifier.getComponentName() + "." + identifier.getModuleName());
		File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptor.isFile()) {
			System.err.println(getClass().getName() + " " + identifier + " not in location " + location);
			return null;
		}

		IRhenaModule module = new RhenaModuleParser(this, identifier, moduleDescriptor.toURI()).getModel();
		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaContext context, IRhenaModule module, EExecutionType type) throws RhenaException {

		// lifecycle dependency chain
		// create dependency chains
		// produce classloader

		if (module.getLifecycleName() == null) {
			throw new RhenaException("Trying to build a module with no lifecycle declaration " + module.getIdentifier());
		}

		try {
			return new RhenaExecution(module.getIdentifier(), type, new ArtifactDescriptor("someartifact", new URL("http://some.url.com/"), "not-implemented"));
		} catch (Exception e) {
			throw new RhenaException(e);
		}
	}

	@Override
	public URI getLocation() {

		return location.toURI();
	}
}
