
package com.unnsvc.rhena.ng.model.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.ng.model.RhenaModule;
import com.unnsvc.rhena.ng.resolution.RhenaModelMaterialiser;

public class LoggingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModelMaterialiser modelMaterialiser;
	private int indents;
	private String label;

	public LoggingVisitor(RhenaModelMaterialiser modelMaterialiser, int indents, String label) {

		this.modelMaterialiser = modelMaterialiser;
		this.indents = indents;
		this.label = label;
		if(label != null) {
			log.info(indent() + "<!-- " + label + " -->");
		}
	}

	public LoggingVisitor(RhenaModelMaterialiser modelMaterialiser) {

		this(modelMaterialiser, 0, null);
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		log.info(indent() + "<" + module.getModuleIdentifier() + ">");
		if (module.getParentModule() != null) {

			RhenaModule parent = modelMaterialiser.materialiseModel(module.getParentModule());
			parent.visit(new LoggingVisitor(modelMaterialiser, indents + 1, "parent declaration:"));
		}

		if (module.getLifecycleDeclaration() != null) {

			RhenaModule lifecycle = modelMaterialiser.materialiseModel(module.getLifecycleDeclaration());
			lifecycle.visit(new LoggingVisitor(modelMaterialiser, indents + 1, "lifecycle declaration:"));
		}
	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		log.info(indent() + "</" + module.getModuleIdentifier() + ">");
	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
