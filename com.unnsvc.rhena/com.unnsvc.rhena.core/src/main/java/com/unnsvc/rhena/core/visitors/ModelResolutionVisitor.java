
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaReference;
import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class ModelResolutionVisitor implements IVisitor {

	private ResolutionManager resolution;

	public ModelResolutionVisitor(ResolutionManager resolution) {

		this.resolution = resolution;
	}

	@Override
	public void startModule(RhenaModel module) throws RhenaException {

		if (module.getParentModule() != null) {
			RhenaReference reference = module.getParentModule();
			RhenaModel model = resolution.materialiseModel(reference.getModuleIdentifier());
			module.setParentModule(model);
			model.visit(this);
		}

		if (module.getLifecycleModule() != null) {
			RhenaReference reference = module.getLifecycleModule();
			RhenaModel model = resolution.materialiseModel(reference.getModuleIdentifier());
			module.setLifecycleModule(model);
			model.visit(this);
		}

		for (RhenaEdge edge : module.getDependencyEdges()) {

			RhenaReference edgeReference = edge.getTarget();
			RhenaModel model = resolution.materialiseModel(edgeReference.getModuleIdentifier());
			edge.setTarget(model);
			model.visit(this);
		}
	}

	@Override
	public void endModule(RhenaModel module) throws RhenaException {

	}

}
