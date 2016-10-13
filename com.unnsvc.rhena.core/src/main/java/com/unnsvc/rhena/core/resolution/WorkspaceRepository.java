
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.WorkspaceProjectMaterialiser;
import com.unnsvc.rhena.core.model.RhenaModule;

public class WorkspaceRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private IResolutionContext context;

	public WorkspaceRepository(IResolutionContext context, File workspaceDirectory) {

		this.context = context;
		this.workspaceDirectory = new File(workspaceDirectory.getAbsoluteFile().toURI().normalize().getPath());
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
//		File moduleDescriptor = new File(workspaceProject, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);

		if (!workspaceProject.exists()) {
			throw new RhenaException(moduleIdentifier.toTag(ExecutionType.MODEL) + ": Not in repository");
		}

		URI projectLocationUri = workspaceProject.toURI();
		RhenaModule model = resolveModel(moduleIdentifier, projectLocationUri);

		return model;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) throws RhenaException {

		WorkspaceProjectMaterialiser etm = new WorkspaceProjectMaterialiser(context, model, type);
		return etm.materialiseExecution();
	}
}
