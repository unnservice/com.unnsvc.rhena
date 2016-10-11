
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
 *
 */
public class ModelResolutionVisitor implements IModelVisitor {

	private IResolutionContext context;

	public ModelResolutionVisitor(IResolutionContext context) {

		this.context = context;
	}

	@Override
	public void startModel(IRhenaModule model) throws RhenaException {
		
		// materialise parent
		if(model.getParentModule() != null && model.getParentModule() instanceof RhenaReference) {
			
			IRhenaModule parent = context.materialiseModel(model.getParentModule().getModuleIdentifier());
			model.setParentModule(parent);
			parent.visit(this);
		}
		
		// materialise references in lifecycle declarations
		for(ILifecycleDeclaration ld : model.getLifecycleDeclarations().values()) {
			
			for(IProcessorReference pr : ld.getProcessors()) {
				
				if(pr.getModule() instanceof RhenaReference) {
					
					IRhenaModule prModule = context.materialiseModel(pr.getModule().getModuleIdentifier());
					pr.setModule(prModule);
					prModule.visit(this);
				}
			}
			
			if(ld.getGenerator().getModule() instanceof RhenaReference) {
				
				IRhenaModule gModule = context.materialiseModel(ld.getGenerator().getModule().getModuleIdentifier());
				ld.getGenerator().setModule(gModule);
				gModule.visit(this);
			}
		}
		
		// materialise references in dependencies
		for(IRhenaEdge edge : model.getDependencyEdges()) {
			
			if(edge.getTarget() instanceof RhenaReference) {
				
				IRhenaModule dep = context.materialiseModel(edge.getTarget().getModuleIdentifier());
				edge.setTarget(dep);
				dep.visit(this);
			}
		}
	}

	@Override
	public void endModel(IRhenaModule model) throws RhenaException {

	}
}
