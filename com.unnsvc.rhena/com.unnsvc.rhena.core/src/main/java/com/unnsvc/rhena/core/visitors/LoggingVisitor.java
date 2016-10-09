
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.RhenaResolutionContext;

public class LoggingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaResolutionContext resolution;
	private int indents;
	private String label;

	public LoggingVisitor(RhenaResolutionContext resolution, int indents, String label) {

		this.resolution = resolution;
		this.indents = indents;
		this.label = label;
		// if(label != null) {
		// log.info(indent() + label);
		// }
	}

	public LoggingVisitor(RhenaResolutionContext resolution) {

		this(resolution, 0, null);
	}

	@Override
	public void startModel(RhenaModel module) throws RhenaException {

		log.info(indent() + (label == null ? "" : "↳" + label + "⇀") + "[" + module.getModuleIdentifier() + "]");

		if (module.getParentModule() != null) {

			RhenaModel parent = resolution.materialiseModel(module.getParentModule().getTarget());
			parent.visit(new LoggingVisitor(resolution, indents + 1, "parent"));
		}

		if (module.getLifecycleModule() != null) {

			RhenaModel lifecycle = resolution.materialiseModel(module.getLifecycleModule().getTarget());
			lifecycle.visit(new LoggingVisitor(resolution, indents + 1, "lifecycle"));
		}

		if (!module.getDependencyEdges().isEmpty()) {

			for (RhenaEdge edge : module.getDependencyEdges()) {

				RhenaModel dependency = resolution.materialiseModel(edge.getTarget());
				dependency.visit(new LoggingVisitor(resolution, indents + 1, "dependency"));
			}
		}
	}

	@Override
	public void endModel(RhenaModel module) throws RhenaException {

	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("   ");
		}
		return sb.toString();
	}
}
