
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.common.lifecycle.ProcessorReference;
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
		
		for(LifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {
			
			for(ProcessorReference processor : lifecycleDeclaration.getProcessors()) {
				
				RhenaModel processorModel = resolver.materialiseModel(processor.getModuleIdentifier());
				processorModel.visit(this);
				resolver.materialiseExecution(processorModel, RhenaExecutionType.COMPILE);
			}
			
			RhenaModel generatorModel = resolver.materialiseModel(lifecycleDeclaration.getGenerator().getModuleIdentifier());
			generatorModel.visit(this);
			resolver.materialiseExecution(generatorModel, RhenaExecutionType.COMPILE);
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
