
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public abstract class AbstractWorkspaceRepository implements IRepository {

	private File location;

	public AbstractWorkspaceRepository(File location) {

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
	public URI getLocation() {

		return location.toURI();
	}
}
