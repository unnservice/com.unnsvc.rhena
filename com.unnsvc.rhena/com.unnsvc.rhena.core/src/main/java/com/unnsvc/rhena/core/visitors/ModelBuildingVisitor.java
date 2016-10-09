
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public class ModelBuildingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext resolver;

	public ModelBuildingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModel model) throws RhenaException {

		if (model.getParentModule() != null) {
			
			resolver.materialiseModel(model.getParentModule()).visit(this);;
		}
		
		if(model.getLifecycleModule() != null) {
			
			RhenaModel lifecycle = resolver.materialiseModel(model.getLifecycleModule());
			lifecycle.visit(this);
			resolver.materialiseExecution(lifecycle, RhenaExecutionType.COMPILE);
		}
		
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			RhenaModel dependency = resolver.materialiseModel(edge.getTarget());
			dependency.visit(this);
			resolver.materialiseExecution(dependency, edge.getExecutionType());
		}
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

		// RhenaExecution execution = getResolver().materialiseModuleType(model,
		// RhenaEdgeType.ITEST);
	}
}
