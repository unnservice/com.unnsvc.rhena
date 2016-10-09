package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.RhenaContext;

public class ModelInitialisationVisitor implements IVisitor {
	
	private RhenaContext context;

	public ModelInitialisationVisitor(RhenaContext context) {
		
		this.context = context;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if(module.getParentModule() != null) {
			context.getResolutionManager().materialiseState(module.getParentModule(), ModuleState.MODEL).visit(this);
		}
		
		if(module.getLifecycleDeclaration() != null) {
			context.getResolutionManager().materialiseState(module.getLifecycleDeclaration(), ModuleState.MODEL).visit(this);
		}
		
		for(RhenaModuleEdge edge : module.getDependencyEdges()) {
			
			context.getResolutionManager().materialiseState(edge.getTarget(), ModuleState.MODEL).visit(this);;
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {


		
	}

}
