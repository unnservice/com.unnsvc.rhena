package com.unnsvc.rhena.core.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.ComponentImportEdge;
import com.unnsvc.rhena.core.model.ProjectDependencyEdge;
import com.unnsvc.rhena.core.model.RhenaNodeEdge;

public class RepositoryManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolver[] resolvers;
	private Map<RhenaNodeEdge, ResolutionResult> cachedEdges = new HashMap<RhenaNodeEdge, ResolutionResult>();

	public RepositoryManager(IResolver... resolvers) {

		this.resolvers = resolvers;
	}

	public ResolutionResult resolveModel(ResolutionEngine engine, RhenaNodeEdge rhenNodeEdge) throws RhenaException {

		log.info("Resolving: " + rhenNodeEdge);

		for (IResolver resolver : filterResolvers(rhenNodeEdge.getResolverName())) {

			ResolutionResult result = null;
			if (rhenNodeEdge instanceof ComponentImportEdge) {

				ComponentImportEdge importEdge = (ComponentImportEdge) rhenNodeEdge;
				if (cachedEdges.containsKey(importEdge)) {

					log.debug("Using cached component: " + importEdge);
					result = cachedEdges.get(importEdge);
				} else {

					result = resolver.resolveComponent(engine, importEdge);
					cachedEdges.put(importEdge, result);
				}
			} else if (rhenNodeEdge instanceof ProjectDependencyEdge) {

				ProjectDependencyEdge projectDepEdge = (ProjectDependencyEdge) rhenNodeEdge;
				if (cachedEdges.containsKey(projectDepEdge)) {

					log.debug("Using cached dependency: " + projectDepEdge);
					result = cachedEdges.get(projectDepEdge);
				} else {

					result = resolver.resolveProject(engine, projectDepEdge);
					cachedEdges.put(projectDepEdge, result);
				}
			}

			switch (result.getStatus()) {
			case SUCCESS:

				return result;
			case FAILURE:

				log.debug(
						"Failed to resolve: " + rhenNodeEdge.toString() + " in repository: " + resolver.getLocation());
				break;
			}
		}

		throw new ResolverException("Failed to resolve: " + rhenNodeEdge.toString() + " in resolvers: " + filterResolvers(rhenNodeEdge.getResolverName()));
	}

	/**
	 * @TODO Does not honor resolver precedence for now
	 * @param resolverName
	 * @return
	 * @throws ResolverException
	 */
	private List<IResolver> filterResolvers(String resolverName) throws ResolverException {

		List<IResolver> filteredResolvers = new ArrayList<IResolver>();
		for (IResolver resolver : this.resolvers) {

			if (resolverName.equals(Constants.DEFAULT_RESOLVER)) {

				if (resolver.getName().equals("workspace") || resolver.getName().equals("repository")) {
					filteredResolvers.add(resolver);
				}
			} else if (resolver.getName().equals(resolverName)) {

				filteredResolvers.add(resolver);
			}
		}

		if (filteredResolvers.isEmpty()) {

			throw new ResolverException("Unknown resolver: " + resolverName);
		}

		return filteredResolvers;
	}
}
