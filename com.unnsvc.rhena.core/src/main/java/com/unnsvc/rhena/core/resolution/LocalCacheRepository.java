
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ArtifactDescriptor;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.ExplodedArtifact;
import com.unnsvc.rhena.common.execution.IArtifact;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.LocalExecution;
import com.unnsvc.rhena.core.execution.RhenaArtifactsDescriptorParser;
import com.unnsvc.rhena.core.model.RhenaModuleParser;

/**
 * @TODO Should also allow for installation..
 * 
 * @author noname
 *
 */
public class LocalCacheRepository implements IRepository {

	private IRhenaContext context;
	private IRhenaConfiguration config;

	public LocalCacheRepository(IRhenaContext context, IRhenaConfiguration config) {

		this.context = context;
		this.config = config;
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		File moduleDirectory = getModuleDirectory(identifier);
		File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!moduleDescriptor.isFile()) {

			context.getLogger().debug(getClass(), identifier, "Not found in repository location: " + getLocationFile());
			return null;
		}

		IRhenaModule module = new RhenaModuleParser(context, this, identifier, moduleDescriptor.toURI()).getModel();
		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, ICaller caller) throws RhenaException {

		File moduleDirectory = getModuleDirectory(caller.getIdentifier());
		if (caller.getExecutionType().equals(EExecutionType.MODEL)) {

			File moduleDescriptor = new File(moduleDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				IArtifact primaryArtifact = new ExplodedArtifact(caller.getIdentifier().toString(), moduleDescriptor.getCanonicalFile().toURI().toURL());
				IArtifactDescriptor descriptor = new ArtifactDescriptor(IArtifactDescriptor.DEFAULT_CLASSIFIER, primaryArtifact);
				return new LocalExecution(caller.getIdentifier(), caller.getExecutionType(), Collections.singletonList(descriptor));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

			File executionDirectory = new File(moduleDirectory, caller.getExecutionType().literal());

			if (!executionDirectory.isDirectory()) {
				return null;
			}

			RhenaArtifactsDescriptorParser execParser = new RhenaArtifactsDescriptorParser(caller.getIdentifier(), caller.getExecutionType(),
					executionDirectory.toURI());
			context.getLogger().debug(getClass(), caller.getIdentifier(), "Created execution: " + execParser.getExecution());
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

		File moduleDirectory = new File(getLocationFile(), relpath.toString());
		return moduleDirectory;
	}

	public File getLocationFile() {

		if(config.getRepositoryConfiguration().getLocalStorageLocation() != null) {
			return config.getRepositoryConfiguration().getLocalStorageLocation();
		}
		return new File(context.getConfig().getInstanceHome(), "repository");
	}

	@Override
	public URI getLocation() {

		return getLocationFile().toURI();
	}

}
