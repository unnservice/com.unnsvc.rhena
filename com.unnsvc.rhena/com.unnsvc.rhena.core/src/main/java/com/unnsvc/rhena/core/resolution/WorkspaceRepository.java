
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.Constants;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.execution.ExecutionTypeMaterialiser;

public class WorkspaceRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private IResolutionContext context;

	public WorkspaceRepository(IResolutionContext context, File workspaceDirectory) {

		this.context = context;
		this.workspaceDirectory = workspaceDirectory;
	}

	@Override
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, Constants.MODULE_DESCRIPTOR_FILENAME);
		URI moduleDescriptorUri = moduleDescriptor.toURI();

		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		RhenaModel model = resolveModel(moduleIdentifier, moduleDescriptorUri);

		return model;
	}

	@Override
	public RhenaExecution materialiseExecution(RhenaModel model, RhenaExecutionType type) throws RhenaException {

		ExecutionTypeMaterialiser etm = new ExecutionTypeMaterialiser(context, type);
		return etm.materialiseExecution(model);
	}
}
