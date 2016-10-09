
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public abstract class ModelResolvingVisitor implements IModelVisitor {

	private IResolutionContext resolver;

	public ModelResolvingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModel model) throws RhenaException {

		if (model.getParentModule() != null) {
			getResolver().materialiseModel(model.getParentModule().getTarget()).visit(this);
		}

		if (model.getLifecycleModule() != null) {
			getResolver().materialiseModel(model.getLifecycleModule().getTarget()).visit(this);
		}

		for (RhenaEdge edge : model.getDependencyEdges()) {

			getResolver().materialiseModel(edge.getTarget()).visit(this);
		}
	}

	public IResolutionContext getResolver() {

		return resolver;
	}

}
