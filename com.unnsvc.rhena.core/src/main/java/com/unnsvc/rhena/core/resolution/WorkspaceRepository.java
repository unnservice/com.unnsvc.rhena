
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.core.execution.ExecutionTypeMaterialiser;
import com.unnsvc.rhena.core.model.RhenaModule;

public class WorkspaceRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private IResolutionContext context;

	public WorkspaceRepository(IResolutionContext context, File workspaceDirectory) {

		this.context = context;
		this.workspaceDirectory = workspaceDirectory;
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptorUri = moduleDescriptor.toURI();

		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		RhenaModule model = resolveModel(moduleIdentifier, moduleDescriptorUri);

		return model;
	}

	@Override
	public RhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) throws RhenaException {

		ExecutionTypeMaterialiser etm = new ExecutionTypeMaterialiser(context, type);
		return etm.materialiseExecution(model);
	}
}
