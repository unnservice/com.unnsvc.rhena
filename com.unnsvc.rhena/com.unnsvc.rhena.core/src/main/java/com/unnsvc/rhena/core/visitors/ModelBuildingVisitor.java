
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public class ModelBuildingVisitor implements IModelVisitor {

	private IResolutionContext resolver;

	public ModelBuildingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModel model) throws RhenaException {

		if (model.getParentModule() != null) {
			
			RhenaModel parent = resolver.materialiseModel(model.getParentModule().getTarget());
			parent.visit(this);
			resolver.materialiseModuleType(model, model.getParentModule().getDependencyType());
		}
		
		if(model.getLifecycleModule() != null) {
			
			RhenaModel lifecycle = resolver.materialiseModel(model.getLifecycleModule().getTarget());
			lifecycle.visit(this);
			resolver.materialiseModuleType(lifecycle, model.getLifecycleModule().getDependencyType());
		}
		
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			RhenaModel dependency = resolver.materialiseModel(edge.getTarget());
			dependency.visit(this);
			resolver.materialiseModuleType(dependency, edge.getDependencyType());
		}
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

		// RhenaExecution execution = getResolver().materialiseModuleType(model,
		// RhenaEdgeType.ITEST);
	}
}
