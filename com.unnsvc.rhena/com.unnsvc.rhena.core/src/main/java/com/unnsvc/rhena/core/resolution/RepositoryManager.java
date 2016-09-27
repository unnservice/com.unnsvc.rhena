package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;

public class RepositoryManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolver[] resolvers;

	public RepositoryManager(IResolver... resolvers) {

		this.resolvers = resolvers;
	}

	public ResolutionResult resolveModel(ResolutionContext context, QualifiedIdentifier qualifiedIdentifier) throws RhenaException {

		log.info("Resolving: " + qualifiedIdentifier);

		for (IResolver resolver : resolvers) {

			ResolutionResult result = null;
			if (qualifiedIdentifier instanceof ComponentIdentifier) {

				result = resolver.resolveComponent(context, (ComponentIdentifier) qualifiedIdentifier);
			} else if (qualifiedIdentifier instanceof ProjectIdentifier) {

				result = resolver.resolveProject(context, (ProjectIdentifier) qualifiedIdentifier);
			}

			switch (result.getStatus()) {
			case SUCCESS:

				return result;
			case FAILURE:

				log.debug("Failed to resolve: " + qualifiedIdentifier.toString() + " in repository: " + resolver.getLocation());
				break;
			}
		}

		throw new ResolverException("Failed to resolve: " + qualifiedIdentifier.toString());
	}
}
