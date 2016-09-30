
package com.unnsvc.rhena;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class RhenaModelBuilder {

	public RhenaModule buildModel(RhenaContext context, String moduleIdentifierStr) throws RhenaException {

		return buildModel(context, ModuleIdentifier.valueOf(moduleIdentifierStr));
	}

	public RhenaModule buildModel(RhenaContext context, ModuleIdentifier moduleIdentifier) throws RhenaException {

		RhenaModule module = context.getResolution().resolveModule(context, moduleIdentifier);
		context.getResolvedIdentifiers().put(moduleIdentifier, module);

		while (!context.getUnresolvedIdentifiers().isEmpty()) {

			if (!context.getResolvedIdentifiers().containsKey(context.getUnresolvedIdentifiers().peek())) {
				RhenaModule inTree = context.getResolution().resolveModule(context, context.getUnresolvedIdentifiers().pop());
				context.getResolvedIdentifiers().put(moduleIdentifier, inTree);
			}
		}

		return module;
	}

}
