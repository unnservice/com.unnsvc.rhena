
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public class RhenaEdgeProcessingVisitor implements IModelVisitor {

	public EdgeCallback callback;
	private IResolver resolver;

	public RhenaEdgeProcessingVisitor(IResolver resolver, EdgeCallback callback) {

		this.resolver = resolver;
		this.callback = callback;
	}

	@Override
	public void startModel(RhenaModel module) throws RhenaException {

		if (module.getParentModule() != null) {

			callback.onModelEdge(module.getParentModule());
			resolver.materialiseModel(module.getParentModule().getTarget()).visit(this);
		}

		if (module.getLifecycleModule() != null) {

			callback.onModelEdge(module.getLifecycleModule());
			resolver.materialiseModel(module.getLifecycleModule().getTarget()).visit(this);
		}

		for (RhenaEdge edge : module.getDependencyEdges()) {

			callback.onModelEdge(edge);
			resolver.materialiseModel(edge.getTarget()).visit(this);
		}
	}

	@Override
	public void endModel(RhenaModel module) throws RhenaException {

	}

	public interface EdgeCallback {

		public void onModelEdge(RhenaEdge edge);
	}
}
