
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.core.execution.RhenaExecutionDescriptorParser;

/**
 * 
 * 
 * Can't really cause a path collision as we check the module.xml contents
 * before using it
 * 
 * <pre>
 * 	workspace		</some/path/repository>/com/component/module1/version/module.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/execution.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/component1.module1-deliverable-version.ext
 * </pre>
 * 
 * @author noname
 *
 */
public class RemoteRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private URI location;

	public RemoteRepository(IResolutionContext context, URI location) {

		super(context);
		this.location = location.normalize();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		StringBuilder moduleDescriptorPath = new StringBuilder(getModuleBase(moduleIdentifier));
		moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptor = Utils.toUri(moduleDescriptorPath.toString());

		if (Utils.exists(moduleDescriptor)) {

			return resolveModel(ModuleType.REMOTE, moduleIdentifier, Utils.toUri(getModuleBase(moduleIdentifier)));
		} else {

			log.debug(moduleIdentifier.toTag(EExecutionType.MODEL) + " was not found at: " + moduleDescriptor.toASCIIString());
			return null;
		}
	}

	private String getModuleBase(ModuleIdentifier moduleIdentifier) {

		StringBuilder moduleBase = new StringBuilder();
		moduleBase.append(location.toString()).append("/");
		moduleBase.append(moduleIdentifier.getComponentName().toString().replaceAll("\\.", "/")).append("/");
		moduleBase.append(moduleIdentifier.getModuleName()).append("/");
		moduleBase.append(moduleIdentifier.getVersion()).append("/");
		return moduleBase.toString();
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		StringBuilder base = new StringBuilder(getModuleBase(module.getModuleIdentifier()));
		base.append(type.toString().toLowerCase()).append("/");

		// ------------ @TODO clean
		StringBuilder executionDescriptorPath = new StringBuilder(base);
		executionDescriptorPath.append(RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
		URI executionDescriptor = Utils.toUri(executionDescriptorPath.toString());
		if (!Utils.exists(executionDescriptor)) {

			throw new NotExistsException("Descriptor does not exist: " + executionDescriptor);
		}
		// ------------

		RhenaExecutionDescriptorParser parser = new RhenaExecutionDescriptorParser(module.getModuleIdentifier(), type, Utils.toUri(base.toString()));
		IRhenaExecution execution = parser.getExecution();

		return execution;
	}

	// Add a publish method which can know whether this repository is local and
	// can install accordingly

}
