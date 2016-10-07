
package com.unnsvc.rhena.builder.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;
import com.unnsvc.rhena.builder.model.RhenaModuleEdge;

public class BuildingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaContext context;
	private CompositeScope scope;

	public BuildingVisitor(RhenaContext context, CompositeScope scope) {

		this.context = context;
		this.scope = scope;
	}

	/**
	 * Here we do some sort of preparation for each module before building it
	 */
	@Override
	public void startVisit(RhenaModuleDescriptor descriptor) throws RhenaException {

		if (descriptor.getParentModule() != null) {

			RhenaModuleDescriptor parentDescriptor = context.getResolution().materialiseScope(context, CompositeScope.MODEL, descriptor.getParentModule());
			parentDescriptor.visit(this);
			// merge parent now?
		}

		if (descriptor.getLifecycleModule() != null) {

			RhenaModuleDescriptor lifecycleDescriptor = context.getResolution().materialiseScope(context, CompositeScope.MODEL, descriptor.getLifecycleModule());
			lifecycleDescriptor.visit(new BuildingVisitor(context, CompositeScope.LIFECYCLE));
		}

		for (RhenaModuleEdge edge : descriptor.getDependencyEdges()) {

			RhenaModuleDescriptor dependencyDescriptor = context.getResolution().materialiseScope(context, edge.getScope(), edge.getTarget());
			dependencyDescriptor.visit(new BuildingVisitor(context, edge.getScope()));
		}
	}

	/**
	 * Here build the actual modules and produce an artifact output for the
	 * lifecycle we're building
	 */
	@Override
	public void endVisit(RhenaModuleDescriptor descriptor) throws RhenaException {
		
		if(scope.getDependency() != null) {
			
			context.getResolution().materialiseScope(context, scope.getDependency(), descriptor.getModuleIdentifier());
		}

		if (!context.getResolvedStates().get(scope).containsKey(descriptor.getModuleIdentifier())) {
			// log.info("Requested " + scope.toString() + " for " +
			// descriptor.getModuleIdentifier());

			RhenaModuleDescriptor moduleDescriptor = descriptor.getRepository().materialiseScope(context, scope, descriptor.getModuleIdentifier());
			context.getResolvedStates().get(scope).put(descriptor.getModuleIdentifier(), moduleDescriptor);
		} else {
			
			log.info("already resolved: " + descriptor.getModuleIdentifier() + ":" + scope.toString().toLowerCase());
		}
	}

}
