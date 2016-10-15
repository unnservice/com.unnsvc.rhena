
package com.unnsvc.rhena.core.visitors;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;
import com.unnsvc.rhena.core.model.RhenaReference;

public class LoggingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private EExecutionType type;
	private int indents;
	private String label;

	public LoggingVisitor(EExecutionType type, IResolutionContext context, int indents, String label) {

		this.type = type;
		this.context = context;
		this.indents = indents;
		this.label = label;
	}

	public LoggingVisitor(EExecutionType type, IResolutionContext context) {

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
				model.getParentModule().getTarget().visit(new LoggingVisitor(EExecutionType.MODEL, context, indents + 1, "parent"));
			}
		}

		if (!model.getDependencyEdges().isEmpty()) {

			for (IRhenaEdge edge : model.getDependencyEdges()) {
				if (!(edge.getTarget() instanceof RhenaReference)) {
					if (type.canTraverse(edge.getExecutionType())) {
						if (!edges.contains(edge)) {
							edges.add(edge);
							edge.getTarget().visit(new LoggingVisitor(type, context, indents + 1, "dependency"));
						}
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
