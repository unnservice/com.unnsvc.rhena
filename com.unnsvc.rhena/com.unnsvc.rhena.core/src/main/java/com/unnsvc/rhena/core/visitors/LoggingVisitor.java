
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class LoggingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResolutionManager resolution;
	private int indents;
	private String label;

	public LoggingVisitor(ResolutionManager resolution, int indents, String label) {

		this.resolution = resolution;
		this.indents = indents;
		this.label = label;
		// if(label != null) {
		// log.info(indent() + label);
		// }
	}

	public LoggingVisitor(ResolutionManager resolution) {

		this(resolution, 0, null);
	}

	@Override
	public void startModule(RhenaModel module) throws RhenaException {

		log.info(indent() + (label == null ? "" : "↳" + label + "⇀") + "[" + module.getModuleIdentifier() + "]");

		if (module.getParentModule() != null) {

			RhenaModel parent = resolution.materialiseModel(module.getParentModule().getModuleIdentifier());
			parent.visit(new LoggingVisitor(resolution, indents + 1, "parent"));
		}

		if (module.getLifecycleModule() != null) {

			RhenaModel lifecycle = resolution.materialiseModel(module.getLifecycleModule().getModuleIdentifier());
			lifecycle.visit(new LoggingVisitor(resolution, indents + 1, "lifecycle"));
		}

		if (!module.getDependencyEdges().isEmpty()) {

			for (RhenaEdge edge : module.getDependencyEdges()) {

				RhenaModel dependency = resolution.materialiseModel(edge.getTarget().getModuleIdentifier());
				dependency.visit(new LoggingVisitor(resolution, indents + 1, "dependency"));
			}
		}
	}

	@Override
	public void endModule(RhenaModel module) throws RhenaException {

	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("   ");
		}
		return sb.toString();
	}
}
