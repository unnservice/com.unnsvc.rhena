
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.WorkspaceProjectMaterialiser;
import com.unnsvc.rhena.core.model.RhenaModule;

public class WorkspaceRepository extends AbstractRepository {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;
	private IResolutionContext context;

	public WorkspaceRepository(IResolutionContext context, File workspaceDirectory) {

		this.context = context;
		this.workspaceDirectory = new File(workspaceDirectory.getAbsoluteFile().toURI().normalize().getPath());
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());

		if (!workspaceProject.exists()) {
			return null;
		}

		URI projectLocationUri = workspaceProject.toURI();
		return resolveModel(moduleIdentifier, projectLocationUri);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule model, EExecutionType type) throws RhenaException {

		WorkspaceProjectMaterialiser etm = new WorkspaceProjectMaterialiser(context, model, type);
		return etm.materialiseExecution();
	}
}
