
package com.unnsvc.rhena.core.resolution;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.ExecutionDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecutionDescriptorParser;
import com.unnsvc.rhena.core.model.RhenaExecution;

//   /some/path/repository/component1/module1/version/module.xml
//   /some/path/repository/component1/module1/version/<execution>/execution.xml
//   /some/path/repository/component1/module1/version/<execution>/component1.module1-deliverable-version.ext
public class RemoteRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private URI location;

	public RemoteRepository(URI location) {

		this.location = location.normalize();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		StringBuilder moduleDescriptorPath = new StringBuilder(getModuleBase(moduleIdentifier));
		moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptor = URI.create(moduleDescriptorPath.toString());

		if (Utils.exists(moduleDescriptor)) {

			return resolveModel(moduleIdentifier, moduleDescriptor);
		} else {
			return null;
		}
	}

	private String getModuleBase(ModuleIdentifier moduleIdentifier) {

		StringBuilder moduleBase = new StringBuilder();
		moduleBase.append(location.toString()).append("/");
		moduleBase.append(moduleIdentifier.getComponentName()).append("/");
		moduleBase.append(moduleIdentifier.getModuleName()).append("/");
		moduleBase.append(moduleIdentifier.getVersion()).append("/");
		return moduleBase.toString();
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, ExecutionType type) throws RhenaException {

		StringBuilder base = new StringBuilder(getModuleBase(module.getModuleIdentifier()));
		base.append(type.toString().toLowerCase()).append("/");

		StringBuilder executionDescriptorPath = new StringBuilder(base);
		executionDescriptorPath.append(RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
		URI executionDescriptor = URI.create(executionDescriptorPath.toString());
		
		
		if(!Utils.exists(executionDescriptor)) {
			return null;
		}

		RhenaExecutionDescriptorParser parser = new RhenaExecutionDescriptorParser(executionDescriptor);
		ExecutionDescriptor descriptor = parser.getDescriptor();
		ArtifactDescriptor artifact = descriptor.getArtifact();

		StringBuilder artifactUrl = new StringBuilder(base.toString());
		artifactUrl.append(artifact.getArtifact());

		/**
		 * @TODO .... the code doesn't quite connect here... how do we check for
		 *       sha1 etc. Just do it like this until production when a solution
		 *       will need to be found
		 */

		try {
			URL url = new URL(artifactUrl.toString());
			RhenaExecution execution = new RhenaExecution(module.getModuleIdentifier(), type, url);
			return execution;
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}

	// Add a publish method which can know whether this repository is local and
	// can install accordingly

}
