
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public class LoggingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private int indents;
	private String label;

	public LoggingVisitor(IResolutionContext context, int indents, String label) {

		this.context = context;
		this.indents = indents;
		this.label = label;
		// if(label != null) {
		// log.info(indent() + label);
		// }
	}

	public LoggingVisitor(IResolutionContext context) {

		this(context, 0, null);
	}

	@Override
	public void startModel(RhenaModel module) throws RhenaException {

		log.info(indent() + (label == null ? "root" : label) + ":" + module.getModuleIdentifier());

		if (module.getParentModule() != null) {

			RhenaModel parent = context.materialiseModel(module.getParentModule());
			parent.visit(new LoggingVisitor(context, indents + 1, "parent"));
		}

		if (module.getLifecycleModule() != null) {

			RhenaModel lifecycle = context.materialiseModel(module.getLifecycleModule());
			lifecycle.visit(new LoggingVisitor(context, indents + 1, "lifecycle"));
		}

		if (!module.getDependencyEdges().isEmpty()) {

			for (RhenaEdge edge : module.getDependencyEdges()) {

				RhenaModel dependency = context.materialiseModel(edge.getTarget());
				dependency.visit(new LoggingVisitor(context, indents + 1, "dependency"));
			}
		}
	}

	@Override
	public void endModel(RhenaModel module) throws RhenaException {

	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}
}
