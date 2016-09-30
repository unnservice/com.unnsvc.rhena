
package com.unnsvc.rhena.builder.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class ResolutionEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;

	public ResolutionEngine(IRepository... repositories) {

		this.repositories = repositories;
	}

	public RhenaModule resolveModule(RhenaContext context, ModuleIdentifier moduleIdentifier) throws RhenaException {

		for (IRepository repository : repositories) {

			try {

				RhenaModule module = repository.resolveModule(moduleIdentifier);
				return module;
			} catch (RhenaException repositoryException) {
				log.debug(repositoryException.getMessage(), repositoryException);
			}
		}

		throw new RhenaException("Unable to resolve: " + moduleIdentifier.toString());
	}
}
