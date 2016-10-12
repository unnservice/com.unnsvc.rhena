
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IConfiguratorReference;
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

		if (module.getLifecycleName() != null) {
			ILifecycleDeclaration lifecycle = module.getLifecycleDeclaration(module.getLifecycleName());

			IConfiguratorReference config = lifecycle.getConfigurator();
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
