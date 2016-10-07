
package com.unnsvc.rhena.ng.resolution;

import java.io.File;
import java.net.URI;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.model.RhenaModule;
import com.unnsvc.rhena.ng.resolution.parsers.RhenaModuleParser;

public class WorkspaceRepository implements IRepository {

	private File workspaceDirectory;

	public WorkspaceRepository(File workspaceDirectory) {

		this.workspaceDirectory = workspaceDirectory;
	}

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File workspaceProject = new File(workspaceDirectory, moduleIdentifier.getComponentName() + "." + moduleIdentifier.getModuleName());
		File moduleDescriptor = new File(workspaceProject, "module.xml");
		if (!moduleDescriptor.exists()) {
			throw new RhenaException("Not in repository");
		}

		URI moduleUri = moduleDescriptor.toURI();
		return new RhenaModuleParser(moduleIdentifier, moduleUri).getModule();
	}
}
