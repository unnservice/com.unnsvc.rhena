
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.RhenaContext;

public class ModelMergeVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaContext context;
	private Set<ModuleIdentifier> merged;

	public ModelMergeVisitor(RhenaContext context) {

		this(context, new HashSet<ModuleIdentifier>());
	}

	public ModelMergeVisitor(RhenaContext context, Set<ModuleIdentifier> merged) {

		this.context = context;
		this.merged = merged;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if (module.getParentModule() != null) {

			RhenaModule parent = context.getResolutionManager().materialiseState(module.getParentModule(), ModuleState.MODEL);
			parent.visit(new ModelMergeVisitor(context, merged));
		}

		if (module.getLifecycleDeclaration() != null) {

			RhenaModule lifecycle = context.getResolutionManager().materialiseState(module.getLifecycleDeclaration(), ModuleState.MODEL);
			lifecycle.visit(new ModelMergeVisitor(context, merged));
		}

		for (RhenaModuleEdge edge : module.getDependencyEdges()) {
			RhenaModule dependency = context.getResolutionManager().materialiseState(edge.getTarget(), ModuleState.MODEL);
			dependency.visit(new ModelMergeVisitor(context, merged));
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		if (!merged.contains(module.getModuleIdentifier())) {
			if (module.getParentModule() != null) {

				RhenaModule parent = context.getResolutionManager().materialiseState(module.getParentModule(), ModuleState.MODEL);

				log.debug("Merge: " + parent.getModuleIdentifier() + " -> " + module.getModuleIdentifier());

				// merge properties
				for (Object key : parent.getProperties().keySet()) {
					module.setProperty((String) key, parent.getProperties().getProperty((String) key));
				}

				// merge dependencies
				for (RhenaModuleEdge edge : parent.getDependencyEdges()) {
					module.addDependencyEdge(edge);
				}

			}
			merged.add(module.getModuleIdentifier());
		}
	}

}
