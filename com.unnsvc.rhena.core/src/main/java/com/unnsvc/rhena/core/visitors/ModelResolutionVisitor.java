
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;
import com.unnsvc.rhena.core.model.RhenaReference;

/**
 * This class will initialise the model by resolving reference to other models.
 * This class can handle cyclic dependency declarations
 * 
 * @author noname
 *
 */
public class ModelResolutionVisitor implements IModelVisitor {

	private IResolutionContext context;
	private Set<ModuleIdentifier> resolved;

	public ModelResolutionVisitor(IResolutionContext context) {

		this.context = context;
		this.resolved = new HashSet<ModuleIdentifier>();
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		resolveModel(module.getParentModule());

		/**
		 * resolve if there's a lifecycle declaration instead of on whether this
		 * module has access to the lifecycle, because we want all model
		 * reference to be resolved for further processing and traversal.
		 */
		for (String key : module.getLifecycleDeclarations().keySet()) {
			ILifecycleDeclaration lifecycle = module.getLifecycleDeclarations().get(key);

			IExecutionReference config = lifecycle.getConfigurator();
			resolveModel(config);

			for (IProcessorReference proc : lifecycle.getProcessors()) {

				resolveModel(proc);
			}

			IGeneratorReference generator = lifecycle.getGenerator();
			resolveModel(generator);
		}

		for (IRhenaEdge edge : module.getDependencyEdges()) {

			resolveModel(edge);
		}
	}

	private void resolveModel(IRhenaEdge parentModule) throws RhenaException {

		if (parentModule != null && parentModule.getTarget() instanceof RhenaReference && !resolved.contains(parentModule.getTarget().getModuleIdentifier())) {

			IRhenaModule resolvedModel = context.materialiseModel(parentModule.getTarget().getModuleIdentifier());

			parentModule.setTarget(resolvedModel);

			resolved.add(resolvedModel.getModuleIdentifier());

			parentModule.getTarget().visit(this);
		}
	}
}
