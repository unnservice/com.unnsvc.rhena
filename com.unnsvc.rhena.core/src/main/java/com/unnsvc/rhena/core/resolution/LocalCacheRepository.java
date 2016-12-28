
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.LocalExecution;
import com.unnsvc.rhena.core.execution.RhenaExecutionDescriptorParser;

/**
 * @TODO Should also allow for installation..
 * 
 * @author noname
 *
 */
public class LocalCacheRepository implements IRepository {

	private IRhenaConfiguration config;
	private File location;

	public LocalCacheRepository(IRhenaConfiguration config) {

		this.config = config;
		this.location = new File(config.getRhenaHome(), "repository");
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		File moduleDirectory = getModuleDirectory(identifier);
		File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptor.isFile()) {

			config.getLogger().debug(getClass(), identifier, "Not found in repository location: " + location);
			return null;
		}

		IRhenaModule module = new RhenaModuleParser(this, identifier, moduleDescriptor.toURI()).getModel();
		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		File moduleDirectory = getModuleDirectory(entryPoint.getTarget());
		if (entryPoint.getExecutionType().equals(EExecutionType.MODEL)) {

			File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				return new LocalExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(),
						moduleDescriptor.getCanonicalFile().toURI().toURL(), Utils.generateSha1(moduleDescriptor)));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

			File executionDirectory = new File(moduleDirectory, entryPoint.getExecutionType().literal());

			if (!executionDirectory.isDirectory()) {
				return null;
			}

			RhenaExecutionDescriptorParser execParser = new RhenaExecutionDescriptorParser(entryPoint.getTarget(), entryPoint.getExecutionType(),
					executionDirectory.toURI());
			config.getLogger().debug(getClass(), entryPoint.getTarget(), "Created execution: " + execParser.getExecution());
			return execParser.getExecution();
		}
		// throw new UnsupportedOperationException("Not implemented for
		// execution: " + entryPoint.getExecutionType().literal());
	}

	private File getModuleDirectory(ModuleIdentifier identifier) {

		StringBuilder relpath = new StringBuilder();
		for (String part : identifier.getComponentName().toString().split("\\.")) {
			relpath.append(part).append(File.separator);
		}
		relpath.append(identifier.getModuleName()).append(File.separator);
		relpath.append(identifier.getVersion().toString()).append(File.separator);

		File moduleDirectory = new File(location, relpath.toString());
		return moduleDirectory;
	}

	@Override
	public URI getLocation() {

		throw new UnsupportedOperationException("Not implemented");
	}

}
