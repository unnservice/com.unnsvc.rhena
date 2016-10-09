
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class ModelResolutionVisitor implements IVisitor {

	private ResolutionManager resolution;

	public ModelResolutionVisitor(ResolutionManager resolution) {

		this.resolution = resolution;
	}

	@Override
	public void startModule(RhenaModel module) throws RhenaException {

		if (module.getParentModule() != null) {
			RhenaModel model = resolution.materialiseModel(module.getParentModule());
			model.visit(this);
		}

		if (module.getLifecycleModule() != null) {
			RhenaModel model = resolution.materialiseModel(module.getLifecycleModule());
			model.visit(this);
		}

		for (RhenaEdge edge : module.getDependencyEdges()) {

			RhenaModel model = resolution.materialiseModel(edge.getTarget());
			model.visit(this);
		}
	}

	@Override
	public void endModule(RhenaModel module) throws RhenaException {

	}

}
