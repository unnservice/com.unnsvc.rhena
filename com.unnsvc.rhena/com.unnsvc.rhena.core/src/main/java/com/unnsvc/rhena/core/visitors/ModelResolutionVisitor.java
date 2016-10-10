
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.common.lifecycle.ProcessorReference;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaReference;

/**
 * This type will traverse the entire model, materialising all model references
 * 
 * @author noname
 *
 */
public class ModelResolutionVisitor implements IModelVisitor {

	private IResolutionContext context;

	public ModelResolutionVisitor(IResolutionContext context) {

		this.context = context;
	}

	@Override
	public void startModel(RhenaModule model) throws RhenaException {
		
		// materialise parent
		if(model.getParentModule() != null && model.getParentModule() instanceof RhenaReference) {
			
			RhenaModule parent = context.materialiseModel(model.getParentModule().getModuleIdentifier());
			model.setParentModule(parent);
			parent.visit(this);
		}
		
		// materialise references in lifecycle declarations
		for(LifecycleDeclaration ld : model.getLifecycleDeclarations().values()) {
			
			for(ProcessorReference pr : ld.getProcessors()) {
				
				if(pr.getModule() instanceof RhenaReference) {
					
					RhenaModule prModule = context.materialiseModel(pr.getModule().getModuleIdentifier());
					pr.setModule(prModule);
					prModule.visit(this);
				}
			}
			
			if(ld.getGenerator().getModule() instanceof RhenaReference) {
				
				RhenaModule gModule = context.materialiseModel(ld.getGenerator().getModule().getModuleIdentifier());
				ld.getGenerator().setModule(gModule);
				gModule.visit(this);
			}
		}
		
		// materialise references in dependencies
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			if(edge.getTarget() instanceof RhenaReference) {
				
				RhenaModule dep = context.materialiseModel(edge.getTarget().getModuleIdentifier());
				edge.setTarget(dep);
				dep.visit(this);
			}
		}
	}

	@Override
	public void endModel(RhenaModule model) throws RhenaException {

	}
}
