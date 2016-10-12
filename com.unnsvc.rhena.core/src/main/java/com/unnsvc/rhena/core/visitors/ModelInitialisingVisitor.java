
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.model.RhenaReference;

/**
 * This type will traverse the entire model, materialising all model references
 * 
 * @author noname
 * @TODO implement basic cyclic logic (More advanced will require checking on
 *       executions instead of just module to module)
 *
 */
public class ModelInitialisingVisitor implements IModelVisitor {

	private IResolutionContext context;

	public ModelInitialisingVisitor(IResolutionContext context) {

		this.context = context;
	}

	@Override
	public void startModule(IRhenaModule model) throws RhenaException {

		// materialise parent
		if (model.getParentModule() != null && model.getParentModule() instanceof RhenaReference) {

			IRhenaModule parent = context.materialiseModel(model.getParentModule().getModuleIdentifier());
			model.setParentModule(parent);
			parent.visit(this);
		}

		// materialise references in lifecycle declarations
		for (ILifecycleDeclaration ld : model.getLifecycleDeclarations().values()) {
			
			if(ld.getConfigurator().getModule() instanceof RhenaReference) {
				
				IRhenaModule module = context.materialiseModel(ld.getConfigurator().getModule().getModuleIdentifier());
				module.visit(this);
				ld.getConfigurator().setModule(module);
			}

			for (IProcessorReference pr : ld.getProcessors()) {

				if (pr.getModule() instanceof RhenaReference) {

					IRhenaModule prModule = context.materialiseModel(pr.getModule().getModuleIdentifier());
					prModule.visit(this);
					pr.setModule(prModule);
				}
			}

			if (ld.getGenerator().getModule() instanceof RhenaReference) {

				IRhenaModule gModule = context.materialiseModel(ld.getGenerator().getModule().getModuleIdentifier());
				gModule.visit(this);
				ld.getGenerator().setModule(gModule);
			}
		}

		// materialise references in dependencies
		for (IRhenaEdge edge : model.getDependencyEdges()) {

			if (edge.getTarget() instanceof RhenaReference) {

				IRhenaModule dep = context.materialiseModel(edge.getTarget().getModuleIdentifier());
				edge.setTarget(dep);
				dep.visit(this);
			}
		}
	}

	@Override
	public void endModule(IRhenaModule model) throws RhenaException {

	}
}
