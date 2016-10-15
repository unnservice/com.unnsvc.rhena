
package com.unnsvc.rhena.core.visitors;

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
		// if(label != null) {
		// log.info(indent() + label);
		// }
	}

	public LoggingVisitor(EExecutionType type, IResolutionContext context) {

		this(type, context, 0, null);
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		log.info(indent() + (label == null ? "root" : label) + ":" + model.getModuleIdentifier());

		if (model.getParentModule() != null) {

			model.getParentModule().getTarget().visit(new LoggingVisitor(type, context, indents + 1, "parent"));
		}

		/**
		 * To debug print the lifecycle, we need to get the declaration or
		 * something or output as structured xml
		 */
//		if (model.getLifecycleName() != null) {
//			ILifecycleDeclaration lifecycleDeclaration = model.getLifecycleDeclaration(model.getLifecycleName());
//
//			lifecycleDeclaration.getContext().getTarget().visit(new LoggingVisitor(context, indents + 1, "context"));
//
//			for (IProcessorReference processor : lifecycleDeclaration.getProcessors()) {
//
//				processor.getTarget().visit(new LoggingVisitor(context, indents + 1, "processor"));
//			}
//
//			lifecycleDeclaration.getGenerator().getTarget().visit(new LoggingVisitor(context, indents + 1, "generator"));
//		}

		if (!model.getDependencyEdges().isEmpty()) {

			for (IRhenaEdge edge : model.getDependencyEdges()) {
				if(!(edge.getTarget() instanceof RhenaReference) && edge.getExecutionType().isA(type)) {
					edge.getTarget().visit(new LoggingVisitor(type, context, indents + 1, "dependency"));
				}
			}
		}
	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}
}
