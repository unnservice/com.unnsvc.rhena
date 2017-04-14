
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.RhenaModuleParser;

public abstract class AbstractWorkspaceRepository implements IRepository {

	protected IRhenaContext context;
	private File location;

	public AbstractWorkspaceRepository(IRhenaContext context, File location) {

		this.context = context;
		this.location = location.getAbsoluteFile();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		File moduleDirectory = new File(location, identifier.getComponentName() + "." + identifier.getModuleName());
		File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptor.isFile()) {

			context.getLogger().debug(getClass(), identifier, "Not found in repository location: " + location);
			return null;
		}

		IRhenaModule module = new RhenaModuleParser(context, this, identifier, moduleDescriptor.toURI()).getModel();
		return module;
	}

	@Override
	public URI getLocation() {

		return location.toURI();
	}
	
	
	public IRhenaContext getContext() {

		return context;
	}
}
