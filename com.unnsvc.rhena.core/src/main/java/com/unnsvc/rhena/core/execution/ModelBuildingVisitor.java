
package com.unnsvc.rhena.core.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class ModelBuildingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext resolver;

	public ModelBuildingVisitor(IResolutionContext resolver) {

		this.resolver = resolver;
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		if (model.getParentModule() != null) {

			model.getParentModule().getTarget().visit(this);
		}

		for (ILifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {

			IExecutionReference configurator = lifecycleDeclaration.getConfigurator();
			configurator.getTarget().visit(this);
			resolver.materialiseExecution(configurator.getTarget(), ExecutionType.FRAMEWORK);

			for (IProcessorReference processor : lifecycleDeclaration.getProcessors()) {

				IRhenaModule processorModel = processor.getTarget();

				resolver.materialiseExecution(processorModel, ExecutionType.FRAMEWORK);
			}

			IRhenaModule generatorModel = lifecycleDeclaration.getGenerator().getTarget();
			generatorModel.visit(this);
			resolver.materialiseExecution(generatorModel, ExecutionType.FRAMEWORK);
		}

		for (IRhenaEdge edge : model.getDependencyEdges()) {

			IRhenaModule dependency = edge.getTarget();
			dependency.visit(this);
			resolver.materialiseExecution(dependency, edge.getExecutionType());
		}
	}
}
