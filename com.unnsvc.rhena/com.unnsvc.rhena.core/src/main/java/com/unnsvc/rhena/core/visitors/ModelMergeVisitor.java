
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class ModelMergeVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResolutionManager resolution;
	private Set<ModuleIdentifier> merged;

	public ModelMergeVisitor(ResolutionManager resolution) {

		this(resolution, new HashSet<ModuleIdentifier>());
	}

	public ModelMergeVisitor(ResolutionManager resolution, Set<ModuleIdentifier> merged) {

		this.resolution = resolution;
		this.merged = merged;
	}

	@Override
	public void startModule(RhenaModel module) throws RhenaException {

		if (module.getParentModule() != null) {

			RhenaModel parent = resolution.materialiseModel(module.getParentModule().getModuleIdentifier());
			parent.visit(new ModelMergeVisitor(resolution, merged));
		}

		if (module.getLifecycleModule() != null) {

			RhenaModel lifecycle = resolution.materialiseModel(module.getLifecycleModule().getModuleIdentifier());
			lifecycle.visit(new ModelMergeVisitor(resolution, merged));
		}

		for (RhenaEdge edge : module.getDependencyEdges()) {
			RhenaModel dependency = resolution.materialiseModel(edge.getTarget().getModuleIdentifier());
			dependency.visit(new ModelMergeVisitor(resolution, merged));
		}
	}

	@Override
	public void endModule(RhenaModel module) throws RhenaException {

		if (!merged.contains(module.getModuleIdentifier())) {
			if (module.getParentModule() != null) {

				RhenaModel parent = resolution.materialiseModel(module.getModuleIdentifier());

				log.debug("Merge: " + parent.getModuleIdentifier() + " -> " + module.getModuleIdentifier());

				// merge properties
				for (Object key : parent.getProperties().keySet()) {
					module.setProperty((String) key, parent.getProperties().getProperty((String) key));
				}

				// merge dependencies
				for (RhenaEdge edge : parent.getDependencyEdges()) {
					module.addDependencyEdge(edge);
				}

			}
			merged.add(module.getModuleIdentifier());
		}
	}

}
