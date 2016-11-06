
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class LoggingVisitor implements IModelVisitor {

	private IRhenaLoggingHandler log;
	private IRhenaContext context;
	private EExecutionType type;
	private int indents;
	private String label;

	public LoggingVisitor(EExecutionType type, IRhenaContext context, int indents, String label) {

		this.type = type;
		this.context = context;
		this.indents = indents;
		this.label = label;
		this.log = context.getLogger(getClass());
	}

	public LoggingVisitor(EExecutionType type, IRhenaContext context) {

		this(type, context, 0, null);
	}

	private static Set<IRhenaEdge> edges;

	static {
		edges = new HashSet<IRhenaEdge>();
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		log.info(indent() + (label == null ? "root" : label) + ":" + model.getModuleIdentifier());

		if (model.getParentModule() != null) {

			if (!edges.contains(model.getParentModule())) {

				edges.add(model.getParentModule());
				context.materialiseModel(model.getParentModule().getTarget()).visit(new LoggingVisitor(EExecutionType.MODEL, context, indents + 1, "parent"));
			}
		}

		if (!model.getDependencyEdges().isEmpty()) {

			for (IRhenaEdge edge : model.getDependencyEdges()) {
				if (type.canTraverse(edge.getExecutionType())) {
					if (!edges.contains(edge)) {
						edges.add(edge);
						context.materialiseModel(edge.getTarget()).visit(new LoggingVisitor(type, context, indents + 1, "dependency"));
					}
				}
			}
		}
	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
