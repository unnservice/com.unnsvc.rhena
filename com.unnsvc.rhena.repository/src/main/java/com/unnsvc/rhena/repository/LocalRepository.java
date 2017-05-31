
package com.unnsvc.rhena.repository;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.model.parser.RhenaModuleParser;

public class LocalRepository extends AbstractRepository {

	// private File location;

	public LocalRepository(IRepositoryDefinition definition) {

		super(definition);
		// this.location = new File(definition.getLocation().getPath());
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) throws RhenaException {

		URI location = getDefinition().getLocation();
		File locationFile = new File(location.getPath());

		String component = moduleIdentifier.getComponentName().toString();
		String moduleName = moduleIdentifier.getModuleName().toString();
		String version = moduleIdentifier.getVersion().toString();

		String modulePath = component + File.separator + moduleName + File.separator + version;

		File moduleLocation = new File(locationFile, modulePath);
		File moduleDescriptorLocation = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptorLocation.isFile()) {
			throw new NotFoundException(moduleIdentifier + " descriptor not found in repository at location: " + moduleDescriptorLocation);
		}

		RhenaModuleParser parser = new RhenaModuleParser(getDefinition().getIdentifier(), moduleIdentifier, moduleDescriptorLocation.toURI());
		IRhenaModule module = parser.getModule();
		module.setModuleType(EModuleType.REMOTE);
		return module;
	}

}
