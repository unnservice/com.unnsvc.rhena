package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public abstract class ModelResolvingVisitor implements IModelVisitor {
	
	private IResolver resolver;
	
	public ModelResolvingVisitor(IResolver resolver) {
		
		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModel model) throws RhenaException {

		if(model.getParentModule() != null) {
			resolver.materialiseModel(model.getParentModule().getTarget()).visit(this);
		}
		
		if(model.getLifecycleModule() != null) {
			resolver.materialiseModel(model.getLifecycleModule().getTarget()).visit(this);
		}
		
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			resolver.materialiseModel(edge.getTarget()).visit(this);
		}
	}

}
