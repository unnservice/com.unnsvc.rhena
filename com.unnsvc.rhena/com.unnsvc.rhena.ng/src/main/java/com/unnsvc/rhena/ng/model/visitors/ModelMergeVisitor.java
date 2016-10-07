
package com.unnsvc.rhena.ng.model.visitors;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleEdge;
import com.unnsvc.rhena.ng.model.RhenaModule;
import com.unnsvc.rhena.ng.resolution.RhenaModelMaterialiser;

public class ModelMergeVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModelMaterialiser materialiser;
	private Set<ModuleIdentifier> merged;

	public ModelMergeVisitor(RhenaModelMaterialiser materialiser) {

		this(materialiser, new HashSet<ModuleIdentifier>());
	}

	public ModelMergeVisitor(RhenaModelMaterialiser materialiser, Set<ModuleIdentifier> merged) {

		this.materialiser = materialiser;
		this.merged = merged;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if (module.getParentModule() != null) {

			RhenaModule parent = materialiser.materialiseModel(module.getParentModule());
			parent.visit(new ModelMergeVisitor(materialiser, merged));
		}

		if (module.getLifecycleDeclaration() != null) {

			RhenaModule lifecycle = materialiser.materialiseModel(module.getLifecycleDeclaration());
			lifecycle.visit(new ModelMergeVisitor(materialiser, merged));
		}

		for (RhenaModuleEdge edge : module.getDependencyEdges()) {
			RhenaModule dependency = materialiser.materialiseModel(edge.getTarget());
			dependency.visit(new ModelMergeVisitor(materialiser, merged));
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		if (!merged.contains(module.getModuleIdentifier())) {
			if (module.getParentModule() != null) {

				RhenaModule parent = materialiser.materialiseModel(module.getParentModule());

				log.info("Merging properties of " + parent.getModuleIdentifier() + " -> " + module.getModuleIdentifier());

				for (Object key : parent.getProperties().keySet()) {
					module.setProperty((String) key, parent.getProperties().getProperty((String) key));
				}
			}
			merged.add(module.getModuleIdentifier());
		}
	}

}
