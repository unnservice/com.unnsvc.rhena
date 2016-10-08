package com.unnsvc.rhena.core.model.visitors;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.resolution.RhenaModelMaterialiser;

public class ModelInitialisationVisitor implements IVisitor {
	
	private RhenaModelMaterialiser materialiser;

	public ModelInitialisationVisitor(RhenaModelMaterialiser materialiser) {
		
		this.materialiser = materialiser;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if(module.getParentModule() != null) {
			materialiser.materialiseModel(module.getParentModule()).visit(this);;
		}
		
		if(module.getLifecycleDeclaration() != null) {
			materialiser.materialiseModel(module.getLifecycleDeclaration()).visit(this);;
		}
		
		for(RhenaModuleEdge edge : module.getDependencyEdges()) {
			
			materialiser.materialiseModel(edge.getTarget()).visit(this);;
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {


		
	}

}
