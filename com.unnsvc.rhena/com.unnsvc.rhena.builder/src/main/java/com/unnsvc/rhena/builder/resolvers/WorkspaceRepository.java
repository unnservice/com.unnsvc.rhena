
package com.unnsvc.rhena.builder.resolvers;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.Constants;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;
import com.unnsvc.rhena.builder.model.RhenaModuleParser;

public class WorkspaceRepository implements RhenaMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspacePath;

	public WorkspaceRepository(File workspacePath) {

		this.workspacePath = workspacePath.getAbsoluteFile();
	}

	@Override
	public RhenaModuleDescriptor materialiseScope(RhenaContext context, CompositeScope scope, ModuleIdentifier moduleIdentifier) throws RhenaException {

		try {
			switch (scope) {
				case MODEL:
					File moduleDirectory = new File(workspacePath, moduleIdentifier.toModuleDirectoryName());
					File moduleDescriptor = new File(moduleDirectory, Constants.MODULE_DESCRIPTOR_FILENAME).getAbsoluteFile();

					RhenaModuleParser moduleParser = new RhenaModuleParser(this);
					RhenaModuleDescriptor module = moduleParser.parse(moduleIdentifier.getModuleName().toString(), moduleDescriptor.toURI());
					if (module.getModuleIdentifier().getVersion().equals(moduleIdentifier.getVersion())) {
						return module;
					} else {
						throw new RhenaException("Unable to find module descriptor for: " + moduleIdentifier);
					}
				default:
					// Default is to state transition
					return transitionToState(context, scope, moduleIdentifier);
			}
		} catch (Exception ex) {
			throw new RhenaException("Unable to resolve descriptor for: " + moduleIdentifier.toString(), ex);
		}
	}

	private RhenaModuleDescriptor transitionToState(RhenaContext context, CompositeScope scope, ModuleIdentifier moduleIdentifier) {
		
		log.info("Transitioning " + moduleIdentifier.toString() + " to " + scope.toString().toLowerCase());
		return null;
	}
}
