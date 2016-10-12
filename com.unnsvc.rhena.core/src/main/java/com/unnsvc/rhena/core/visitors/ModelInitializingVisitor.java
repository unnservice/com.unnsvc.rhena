
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
public class ModelInitializingVisitor implements IModelVisitor {

	private IResolutionContext context;
	private Set<ModuleIdentifier> resolved;

	public ModelInitializingVisitor(IResolutionContext context) {

		this.context = context;
		this.resolved = new HashSet<ModuleIdentifier>();
	}

	@Override
	public void startModule(IRhenaModule module) throws RhenaException {

		if (module.getParentModule() != null && (module.getParentModule().getTarget() instanceof RhenaReference)
				&& !resolved.contains(module.getParentModule().getTarget())) {

			IRhenaModule parent = context.materialiseModel(module.getParentModule().getTarget().getModuleIdentifier());
			parent.visit(this);
			module.getParentModule().setTarget(parent);
			resolved.add(parent.getModuleIdentifier());
		}

		if (module.getLifecycleName() != null) {
			ILifecycleDeclaration lifecycle = module.getLifecycleDeclaration(module.getLifecycleName());

			IConfiguratorReference config = lifecycle.getConfigurator();
			if (config != null && config.getModule() instanceof RhenaReference && !resolved.contains(config.getModule().getModuleIdentifier())) {

				IRhenaModule configModule = context.materialiseModel(config.getModule().getModuleIdentifier());
				config.setModule(configModule);
				configModule.visit(this);
				resolved.add(configModule.getModuleIdentifier());
			}

			for (IProcessorReference proc : lifecycle.getProcessors()) {

				if (proc.getModule() instanceof RhenaReference && !resolved.contains(proc.getModule().getModuleIdentifier())) {
					IRhenaModule procModule = context.materialiseModel(proc.getModule().getModuleIdentifier());
					proc.setModule(procModule);
					procModule.visit(this);
					resolved.add(proc.getModule().getModuleIdentifier());
				}
			}

			IGeneratorReference generator = lifecycle.getGenerator();
			if (generator != null && generator.getModule() instanceof RhenaReference && !resolved.contains(generator.getModule().getModuleIdentifier())) {

				IRhenaModule genModule = context.materialiseModel(generator.getModule().getModuleIdentifier());
				generator.setModule(genModule);
				genModule.visit(this);
				resolved.add(genModule.getModuleIdentifier());
			}
		}

		for (IRhenaEdge edge : module.getDependencyEdges()) {

			if (edge.getTarget() instanceof RhenaReference && !resolved.contains(edge.getTarget().getModuleIdentifier())) {
				
				IRhenaModule depMod = context.materialiseModel(edge.getTarget().getModuleIdentifier());
				edge.setTarget(depMod);
				depMod.visit(this);
				resolved.add(edge.getTarget().getModuleIdentifier());
			}
		}
	}

	@Override
	public void endModule(IRhenaModule module) throws RhenaException {

	}

}
