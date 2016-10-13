
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

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
	public IRhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) {

		StringBuilder executionDescriptorPath = new StringBuilder(getModuleBase(model.getModuleIdentifier()));
		executionDescriptorPath.append(RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
		URI executionDescriptor = URI.create(executionDescriptorPath.toString());

		throw new UnsupportedOperationException("Not implemented");

	}

}
