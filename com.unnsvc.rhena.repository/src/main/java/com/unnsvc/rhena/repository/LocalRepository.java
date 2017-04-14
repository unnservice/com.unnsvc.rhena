
package com.unnsvc.rhena.repository;

import java.io.File;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;
import com.unnsvc.rhena.model.parser.RhenaModuleParser;

public class LocalRepository extends AbstractRepository {

	private File location;

	public LocalRepository(RepositoryIdentifier identifier, File location, IRhenaCache cache) {

		super(identifier, cache);
		this.location = location;
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) throws RhenaException {

		String component = moduleIdentifier.getComponentName().toString();
		String module = moduleIdentifier.getModuleName().toString();
		String version = moduleIdentifier.getVersion().toString();

		String modulePath = component + File.separator + module + File.separator + version;

		File moduleLocation = new File(location, modulePath);
		File moduleDescriptorLocation = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptorLocation.isFile()) {
			throw new NotFoundException(moduleIdentifier + " descriptor not found in repository at location: " + moduleDescriptorLocation);
		}

		RhenaModuleParser parser = new RhenaModuleParser(getCache(), getIdentifier(), moduleIdentifier, moduleDescriptorLocation.toURI());
		return parser.getModule();
	}

}
