
package com.unnsvc.rhena.core.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.common.lifecycle.ProcessorReference;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class ModelBuildingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext resolver;

	public ModelBuildingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void startModel(RhenaModule model) throws RhenaException {

		if (model.getParentModule() != null) {
			
			model.getParentModule().visit(this);
		}
		
		for(LifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {
			
			for(ProcessorReference processor : lifecycleDeclaration.getProcessors()) {
				
				RhenaModule processorModel = processor.getModule();
				
				resolver.materialiseExecution(processorModel, ExecutionType.COMPILE);
			}
			
			RhenaModule generatorModel = lifecycleDeclaration.getGenerator().getModule();
			generatorModel.visit(this);
			resolver.materialiseExecution(generatorModel, ExecutionType.COMPILE);
		}
		
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			RhenaModule dependency = edge.getTarget();
			dependency.visit(this);
			resolver.materialiseExecution(dependency, edge.getExecutionType());
		}
	}

	@Override
	public void endModel(RhenaModule model) throws RhenaException {

		// RhenaExecution execution = getResolver().materialiseModuleType(model,
		// RhenaEdgeType.ITEST);
	}
}
