
package com.unnsvc.rhena.builder.resolvers;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.Constants;
import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.model.RhenaModuleParser;

public class WorkspaceRepository implements IRepository {

	private Logger log =  LoggerFactory.getLogger(getClass());
	private File workspacePath;

	public WorkspaceRepository(File workspacePath) {

		this.workspacePath = workspacePath;
	}

	@Override
	public RhenaModule resolveModule(ModuleIdentifier moduleIdentifier) throws RhenaException {

		File moduleDirectory = new File(workspacePath, moduleIdentifier.toModuleDirectoryName());
		File moduleDescriptor = new File(moduleDirectory, Constants.MODULE_DESCRIPTOR_FILENAME).getAbsoluteFile();
		
		RhenaModuleParser moduleParser = new RhenaModuleParser(moduleIdentifier);
		try {
			RhenaModule module = moduleParser.parse(moduleIdentifier.getModuleName().toString(), moduleDescriptor.toURI());
			if(module.getModuleIdentifier().getVersion().equals(moduleIdentifier.getVersion())) {
				return module;
			} else {
				throw new RhenaException("Unable to find module descfriptor for: " + moduleIdentifier);
			}
		} catch (Exception ex) {
			throw new RhenaException("Unable to resolve descriptor for: " + moduleIdentifier.toString(), ex);
		}			
	}
}
