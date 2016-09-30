
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.RhenaComponent;
import com.unnsvc.rhena.core.model.RhenaComponentEdge;
import com.unnsvc.rhena.core.model.RhenaDependencyEdge;
import com.unnsvc.rhena.core.model.RhenaProject;

/**
 * @TODO create a guard with canResolve on the resolvers, so we don't have maven
 *       artifacts resolved in workspace etc.
 * @author noname
 *
 */
public class RhenaRepositoryManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaResolver[] resolvers;

	public RhenaRepositoryManager(RhenaResolver... resolvers) {

		this.resolvers = resolvers;
	}

	public RhenaComponentEdge resolveComponentEdge(ResolutionEngine resolutionEngine, RhenaComponentEdge edge) throws RhenaException {

		for (RhenaResolver resolver : resolvers) {

			try {
				RhenaComponent component = resolver.resolveComponent(edge.getIdentifier());
				edge.setComponent(component);
			} catch (ResolverException re) {

				log.debug(re.getMessage(), re);
			}
			return edge;
		}

		throw new ResolverException("Failed to resolve: " + edge.getIdentifier());
	}

	public RhenaDependencyEdge resolveDependencyEdge(RhenaDependencyEdge edge) throws RhenaException {

		for (RhenaResolver resolver : resolvers) {

			try {
				RhenaProject project = resolver.resolveProject(edge.getIdentifier());
				edge.setProject(project);
				return edge;
			} catch (ResolverException re) {
				log.debug(re.getMessage(), re);
			}
		}

		throw new ResolverException("Failed to resolve: " + edge.getIdentifier());
	}

	// public ResolutionResult resolveModel(ResolutionEngine engine, RhenaEdge
	// rhenNodeEdge) throws RhenaException {
	//
	// log.info("Resolving: " + rhenNodeEdge);
	//
	// for (RhenaResolver resolver :
	// filterResolvers(rhenNodeEdge.getResolverName())) {
	//
	// ResolutionResult result = null;
	// if (rhenNodeEdge instanceof RhenaComponentEdge) {
	//
	// RhenaComponentEdge importEdge = (RhenaComponentEdge) rhenNodeEdge;
	// if (cachedEdges.containsKey(importEdge)) {
	//
	// log.debug("Using cached component: " + importEdge);
	// result = cachedEdges.get(importEdge);
	// } else {
	//
	// result = resolver.resolveComponent(engine, importEdge);
	// cachedEdges.put(importEdge, result);
	// }
	// } else if (rhenNodeEdge instanceof ProjectDependencyEdge) {
	//
	// ProjectDependencyEdge projectDepEdge = (ProjectDependencyEdge)
	// rhenNodeEdge;
	// if (cachedEdges.containsKey(projectDepEdge)) {
	//
	// log.debug("Using cached dependency: " + projectDepEdge);
	// result = cachedEdges.get(projectDepEdge);
	// } else {
	//
	// result = resolver.resolveProject(engine, projectDepEdge);
	// cachedEdges.put(projectDepEdge, result);
	// }
	// }
	//
	// switch (result.getStatus()) {
	// case SUCCESS:
	//
	// return result;
	// case FAILURE:
	//
	// log.debug("Failed to resolve: " + rhenNodeEdge.toString() + " in
	// repository: " + resolver.getLocation());
	// break;
	// }
	// }
	//
	// throw new ResolverException("Failed to resolve: " +
	// rhenNodeEdge.toString() + " in resolvers: " +
	// filterResolvers(rhenNodeEdge.getResolverName()));
	// }
	//
	// /**
	// * @TODO Does not honor resolver precedence for now
	// * @param resolverName
	// * @return
	// * @throws ResolverException
	// */
	// private List<RhenaResolver> filterResolvers(String resolverName) throws
	// ResolverException {
	//
	// List<RhenaResolver> filteredResolvers = new ArrayList<RhenaResolver>();
	// for (RhenaResolver resolver : this.resolvers) {
	//
	// if (resolverName.equals(Constants.DEFAULT_RESOLVER)) {
	//
	// if (resolver.getName().equals("workspace") ||
	// resolver.getName().equals("repository")) {
	// filteredResolvers.add(resolver);
	// }
	// } else if (resolver.getName().equals(resolverName)) {
	//
	// filteredResolvers.add(resolver);
	// }
	// }
	//
	// if (filteredResolvers.isEmpty()) {
	//
	// throw new ResolverException("Unknown resolver: " + resolverName);
	// }
	//
	// return filteredResolvers;
	// }
	//
	// public ResolutionResult requestResolution(ResolutionRequest
	// resolutionRequest) throws ResolverException {
	//
	// }
}
