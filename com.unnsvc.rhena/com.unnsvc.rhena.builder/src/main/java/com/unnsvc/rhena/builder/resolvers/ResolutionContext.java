
package com.unnsvc.rhena.builder.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;

public class ResolutionContext {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaMaterialiser[] repositories;

	public ResolutionContext(RhenaMaterialiser... repositories) {

		this.repositories = repositories;
	}

	public RhenaModuleDescriptor materialiseScope(RhenaContext context, CompositeScope scope, ModuleIdentifier moduleIdentifier) throws RhenaException {

		for (RhenaMaterialiser repository : repositories) {

			try {

				RhenaModuleDescriptor descriptor = repository.materialiseScope(context, scope, moduleIdentifier);
				log.info("Resolved " + moduleIdentifier + ":" + scope.toString().toLowerCase());
				return descriptor;
			} catch (RhenaException repositoryException) {
				log.debug(repositoryException.getMessage(), repositoryException);
			}
		}

		throw new RhenaException("Unable to resolve: " + moduleIdentifier.toString());
	}
}
