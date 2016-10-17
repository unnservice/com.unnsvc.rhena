	
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.core.execution.WorkspaceProjectMaterialiser;
import com.unnsvc.rhena.core.model.RhenaModule;

public class WorkspaceRepository extends AbstractRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceDirectory;

	public WorkspaceRepository(IRhenaContext context, File workspaceDirectory) {

		super(context);
		this.workspaceDirectory = new File(workspaceDirectory.getAbsoluteFile().toURI().normalize().getPath()).getAbsoluteFile();
		log.info("Workspace at: " + this.workspaceDirectory);
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());

		if (!workspaceProject.exists()) {
			return null;
		}

		URI projectLocationUri = workspaceProject.toURI();
		return resolveModel(ModuleType.WORKSPACE, moduleIdentifier, projectLocationUri);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule model, EExecutionType type) throws RhenaException {

		WorkspaceProjectMaterialiser etm = new WorkspaceProjectMaterialiser(getContext());
		return etm.materialiseExecution(model, type);
	}
}
