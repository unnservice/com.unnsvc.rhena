
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * 
 * 
 * Can't really cause a path collision as we check the module.xml contents
 * before using it
 * 
 * <pre>
 * 	workspace		</some/path/repository>/com/component/module1/version/module.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/artifacts.xml
 *  remote			</some/path/repository>/com/component/module1/version/<execution>/component1.module1-deliverable-version.ext
 * </pre>
 * 
 * @author noname
 *
 */
public class RemoteRepository implements IRepository {

	private IRhenaContext context;

	public RemoteRepository(IRhenaContext context, URI location) {

		this.context = context;
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		// StringBuilder moduleDescriptorPath = new
		// StringBuilder(getModuleBase(moduleIdentifier));
		// moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		// URI moduleDescriptor = Utils.toUri(moduleDescriptorPath.toString());
		//
		// if (Utils.exists(moduleDescriptor)) {
		//
		// return new RhenaModuleParser(context, this, moduleIdentifier,
		// moduleDescriptor).getModel();
		// } else {
		//
		// return null;
		// }
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, ICaller caller) throws RhenaException {

		// IRhenaModule module = caller.getModule();
		// StringBuilder moduleDescriptorPath = new
		// StringBuilder(getModuleBase(module.getIdentifier()));

		throw new UnsupportedOperationException("Not implemented");
	}

	// private String getModuleBase(ModuleIdentifier moduleIdentifier) {
	//
	// StringBuilder moduleBase = new StringBuilder();
	// moduleBase.append(repoDef.getUri().toString()).append("/");
	// moduleBase.append("main").append("/");
	// moduleBase.append(moduleIdentifier.getComponentName().toString().replaceAll("\\.",
	// "/")).append("/");
	// moduleBase.append(moduleIdentifier.getModuleName()).append("/");
	// moduleBase.append(moduleIdentifier.getVersion()).append("/");
	// return moduleBase.toString();
	// }

	@Override
	public URI getLocation() {

		// return repoDef.getUri();
		throw new UnsupportedOperationException("Not implemented");
	}
}
// extends AbstractRepository {
//
// public RemoteRepository(IRhenaContext context) {
//
// super(context);
// }
//
// @Override
// public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier)
// throws RhenaException {
//
//
// return null;
// }
//
// @Override
// public IRhenaExecution materialiseExecution(IModelResolver resolver,
// IEntryPoint entryPoint) throws RhenaException {
//
//
// return null;
// }
//
// @Override
// public URI getLocation() {
//
//
// return null;
// }

// private IRhenaLoggingHandler log;
// private URI location;
//
// public RemoteRepository(IRhenaContext context, URI location) {
//
// super(context);
// this.location = location.normalize();
// this.log = context.getLogger(getClass());
// }
//
// @Override
// public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier)
// throws RhenaException {
//
// StringBuilder moduleDescriptorPath = new
// StringBuilder(getModuleBase(moduleIdentifier));
// moduleDescriptorPath.append(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
// URI moduleDescriptor = Utils.toUri(moduleDescriptorPath.toString());
//
// if (Utils.exists(moduleDescriptor)) {
//
// return resolveModel(ModuleType.REMOTE, moduleIdentifier,
// Utils.toUri(getModuleBase(moduleIdentifier)));
// } else {
//
// log.debug(moduleIdentifier, EExecutionType.MODEL, " was not found at: " +
// moduleDescriptor.toASCIIString());
// return null;
// }
// }
//
// private String getModuleBase(ModuleIdentifier moduleIdentifier) {
//
// StringBuilder moduleBase = new StringBuilder();
// moduleBase.append(location.toString()).append("/");
// moduleBase.append(moduleIdentifier.getComponentName().toString().replaceAll("\\.",
// "/")).append("/");
// moduleBase.append(moduleIdentifier.getModuleName()).append("/");
// moduleBase.append(moduleIdentifier.getVersion()).append("/");
// return moduleBase.toString();
// }
//
// @Override
// public IRhenaExecution materialiseExecution(IRhenaModule module,
// EExecutionType type) throws RhenaException {
//
// StringBuilder base = new
// StringBuilder(getModuleBase(module.getModuleIdentifier()));
// base.append(type.toString().toLowerCase()).append("/");
//
// // ------------ @TODO clean
// StringBuilder executionDescriptorPath = new StringBuilder(base);
// executionDescriptorPath.append(RhenaConstants.EXECUTION_DESCRIPTOR_FILENAME);
// URI executionDescriptor = Utils.toUri(executionDescriptorPath.toString());
// if (!Utils.exists(executionDescriptor)) {
//
// throw new NotExistsException("Descriptor does not exist: " +
// executionDescriptor);
// }
// // ------------
//
// RhenaExecutionDescriptorParser parser = new
// RhenaExecutionDescriptorParser(getContext(), module.getModuleIdentifier(),
// type, Utils.toUri(base.toString()));
// IRhenaExecution execution = parser.getExecution();
//
// return execution;
// }
//
// // Add a publish method which can know whether this repository is local and
// // can install accordingly
//
// }
