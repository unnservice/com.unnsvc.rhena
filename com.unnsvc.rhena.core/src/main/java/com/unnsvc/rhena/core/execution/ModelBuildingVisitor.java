
package com.unnsvc.rhena.core.execution;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
//import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class ModelBuildingVisitor implements IModelVisitor {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;

	public ModelBuildingVisitor(IRhenaContext resolver) {

		this.context = resolver;
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		if (model.getParentModule() != null) {

			context.materialiseModel(model.getParentModule().getTarget()).visit(this);
		}

		for (ILifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {

			IExecutionReference configurator = lifecycleDeclaration.getContext();
			context.materialiseModel(configurator.getModuleEdge().getTarget()).visit(this);
			context.materialiseExecution(context.materialiseModel(configurator.getModuleEdge().getTarget()), EExecutionType.FRAMEWORK);

			for (IProcessorReference processor : lifecycleDeclaration.getProcessors()) {

				IRhenaModule processorModel = context.materialiseModel(processor.getModuleEdge().getTarget());

				context.materialiseExecution(processorModel, EExecutionType.FRAMEWORK);
			}

			IRhenaModule generatorModel = context.materialiseModel(lifecycleDeclaration.getGenerator().getModuleEdge().getTarget());
			generatorModel.visit(this);
			context.materialiseExecution(generatorModel, EExecutionType.FRAMEWORK);
		}

		for (IRhenaEdge edge : model.getDependencyEdges()) {

			IRhenaModule dependency = context.materialiseModel(edge.getTarget());
			dependency.visit(this);
			context.materialiseExecution(dependency, edge.getExecutionType());
		}
	}
}
