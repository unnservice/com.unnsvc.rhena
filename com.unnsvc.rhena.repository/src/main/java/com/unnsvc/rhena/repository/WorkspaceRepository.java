
package com.unnsvc.rhena.repository;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.model.parser.RhenaModuleParser;

public class WorkspaceRepository extends LocalRepository {

	public WorkspaceRepository(IRepositoryDefinition definition) {

		super(definition);
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) throws RhenaException {

		URI location = getDefinition().getLocation();
		File locationFile = new File(location.getPath());

		try {
			locationFile = locationFile.getCanonicalFile();
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}

		String component = moduleIdentifier.getComponentName().toString();
		String moduleName = moduleIdentifier.getModuleName().toString();

		File modulePath = new File(locationFile, component + "." + moduleName);
		File descriptorPath = new File(modulePath, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!descriptorPath.isFile()) {
			throw new NotFoundException(moduleIdentifier + " descriptor not found in repository at location: " + descriptorPath);
		}
		
		RhenaModuleParser parser = new RhenaModuleParser(getDefinition().getIdentifier(), moduleIdentifier, descriptorPath.toURI());

		IRhenaModule module = parser.getModule();
		module.setModuleType(EModuleType.WORKSPACE);
		return module;
	}
}
