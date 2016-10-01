
package com.unnsvc.rhena.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class RhenaModelBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());

	public RhenaModule buildModel(RhenaContext context, String moduleIdentifierStr) throws RhenaException {

		String[] parts = moduleIdentifierStr.split(":");
		return buildModel(context, parts[0], parts[1], parts[2]);
	}

	public RhenaModule buildModel(RhenaContext context, String componentIdentifierStr, String moduleNameStr, String version) throws RhenaException {

		ModuleIdentifier entryPointModuleIdentifier = context.newModuleIdentifier(componentIdentifierStr, moduleNameStr, version);
		log.info("Building model for: " + entryPointModuleIdentifier.toString());

		while (!context.getUnresolvedIdentifiers().isEmpty()) {

			ModuleIdentifier moduleIdentifier = context.getUnresolvedIdentifiers().pop();
			if (!context.getModules().containsKey(moduleIdentifier)) {
				log.info("Resolving: " + moduleIdentifier.toString());

				RhenaModule module = context.getResolution().resolveModule(context, moduleIdentifier);
				context.getModules().put(moduleIdentifier, module);
			}
		}

		return context.getModules().get(entryPointModuleIdentifier);
	}

}
