
package com.unnsvc.rhena.core.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class ModelBuildingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext resolver;

	public ModelBuildingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(IRhenaModule model) throws RhenaException {

		if (model.getParentModule() != null) {
			
			model.getParentModule().visit(this);
		}
		
		for(ILifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {
			
			for(IProcessorReference processor : lifecycleDeclaration.getProcessors()) {
				
				IRhenaModule processorModel = processor.getModule();
				
				resolver.materialiseExecution(processorModel, ExecutionType.DELIVERABLE);
			}
			
			IRhenaModule generatorModel = lifecycleDeclaration.getGenerator().getModule();
			generatorModel.visit(this);
			resolver.materialiseExecution(generatorModel, ExecutionType.DELIVERABLE);
		}
		
		for(IRhenaEdge edge : model.getDependencyEdges()) {
			
			IRhenaModule dependency = edge.getTarget();
			dependency.visit(this);
			resolver.materialiseExecution(dependency, edge.getExecutionType());
		}
	}

	@Override
	public void endModel(IRhenaModule model) throws RhenaException {

		// RhenaExecution execution = getResolver().materialiseModuleType(model,
		// RhenaEdgeType.ITEST);
	}
}
