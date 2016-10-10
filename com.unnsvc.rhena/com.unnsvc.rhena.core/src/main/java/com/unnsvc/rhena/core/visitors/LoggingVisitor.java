
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.common.lifecycle.ProcessorReference;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModule;

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
	public void startModel(RhenaModule model) throws RhenaException {

		log.info(indent() + (label == null ? "root" : label) + ":" + model.getModuleIdentifier());

		if (model.getParentModule() != null) {

			model.getParentModule().visit(new LoggingVisitor(context, indents + 1, "parent"));
		}

		
		/**
		 * To debug print the lifecycle, we need to get the declaration or something or output as structured xml
		 */
		
//		for (LifecycleDeclaration lifecycleDeclaration : model.getLifecycleDeclarations().values()) {
//
//			for (ProcessorReference processor : lifecycleDeclaration.getProcessors()) {
//
//				context.materialiseModel(processor.getModuleIdentifier()).visit(new LoggingVisitor(context, indents + 1, "processor"));
//			}
//
//			context.materialiseModel(lifecycleDeclaration.getGenerator().getModuleIdentifier()).visit(new LoggingVisitor(context, indents + 1, "lifecycle"));
//		}

		if (!model.getDependencyEdges().isEmpty()) {

			for (RhenaEdge edge : model.getDependencyEdges()) {

				edge.getTarget().visit(new LoggingVisitor(context, indents + 1, "dependency"));
			}
		}
	}

	@Override
	public void endModel(RhenaModule module) throws RhenaException {

	}

	private String indent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}
}
