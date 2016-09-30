
package com.unnsvc.rhena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class RhenaModelBuilder {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public RhenaModule buildModel(RhenaContext context, String moduleIdentifierStr) throws RhenaException {

		return buildModel(context, ModuleIdentifier.valueOf(moduleIdentifierStr));
	}

	public RhenaModule buildModel(RhenaContext context, ModuleIdentifier moduleIdentifier) throws RhenaException {

		logger.info("Building model for: " + moduleIdentifier.toString());
		
		RhenaModule module = context.getResolution().resolveModule(context, moduleIdentifier);
		if(module.getParentModule() != null) {
			context.getUnresolvedIdentifiers().push(module.getParentModule());
		}
		context.getUnresolvedIdentifiers().push(moduleIdentifier);

		while (!context.getUnresolvedIdentifiers().isEmpty()) {

			if (!context.getResolvedIdentifiers().containsKey(context.getUnresolvedIdentifiers().peek())) {
				RhenaModule inTree = context.getResolution().resolveModule(context, context.getUnresolvedIdentifiers().pop());

				context.getResolvedIdentifiers().put(moduleIdentifier, inTree);
				logger.info("Resolved: " + inTree.getModuleIdentifier().toString());
			}
		}

		return module;
	}

}
