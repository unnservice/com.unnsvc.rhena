package com.unnsvc.rhena.ng.model.visitors;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.model.RhenaModuleEdge;
import com.unnsvc.rhena.ng.model.RhenaModule;
import com.unnsvc.rhena.ng.resolution.RhenaModelMaterialiser;

public class ChainModelResolutionVisitor implements IVisitor {
	
	private RhenaModelMaterialiser materialiser;

	public ChainModelResolutionVisitor(RhenaModelMaterialiser materialiser) {
		
		this.materialiser = materialiser;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if(module.getParentModule() != null) {
			materialiser.materialiseModel(module.getParentModule());
		}
		
		if(module.getLifecycleDeclaration() != null) {
			materialiser.materialiseModel(module.getLifecycleDeclaration());
		}
		
		for(RhenaModuleEdge edge : module.getDependencyEdges()) {
			
			materialiser.materialiseModel(edge.getTarget());
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		// TODO Auto-generated method stub
		
	}

}
