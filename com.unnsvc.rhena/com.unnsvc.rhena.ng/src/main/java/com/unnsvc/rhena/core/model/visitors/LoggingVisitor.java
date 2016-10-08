
package com.unnsvc.rhena.core.model.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.resolution.RhenaModelMaterialiser;

public class LoggingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModelMaterialiser modelMaterialiser;
	private int indents;
	private String label;

	public LoggingVisitor(RhenaModelMaterialiser modelMaterialiser, int indents, String label) {

		this.modelMaterialiser = modelMaterialiser;
		this.indents = indents;
		this.label = label;
//		if(label != null) {
//			log.info(indent() + label);
//		}
	}

	public LoggingVisitor(RhenaModelMaterialiser modelMaterialiser) {

		this(modelMaterialiser, 0, null);
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		log.info(indent() +  (label == null ? "" : "↳" + label + "⇀") + "[" + module.getModuleIdentifier() + "]");
		
		if (module.getParentModule() != null) {

			RhenaModule parent = modelMaterialiser.materialiseModel(module.getParentModule());
			parent.visit(new LoggingVisitor(modelMaterialiser, indents + 1, "parent"));
		}

		if (module.getLifecycleDeclaration() != null) {

			RhenaModule lifecycle = modelMaterialiser.materialiseModel(module.getLifecycleDeclaration());
			lifecycle.visit(new LoggingVisitor(modelMaterialiser, indents + 1, "lifecycle"));
		}
		
		if(!module.getDependencyEdges().isEmpty()) {
			
			for(RhenaModuleEdge edge : module.getDependencyEdges()) {
				
				RhenaModule dependency = modelMaterialiser.materialiseModel(edge.getTarget());
				dependency.visit(new LoggingVisitor(modelMaterialiser, indents + 1, "dependency"));
			}
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("   ");
		}
		return sb.toString();
	}
}
