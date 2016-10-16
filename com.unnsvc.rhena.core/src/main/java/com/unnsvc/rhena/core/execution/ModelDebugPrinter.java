
package com.unnsvc.rhena.core.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;

public class ModelDebugPrinter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private IResolutionContext context;
	private GraphResolver graphResolver;

	public ModelDebugPrinter(IResolutionContext context, GraphResolver graphResovler) {

		this.context = context;
		this.graphResolver = graphResovler;
	}

	public void debugPrint(IRhenaModule model, EExecutionType type) throws RhenaException {

		/**
		 * Test debug log
		 */
		for (ILifecycleDeclaration lifecycle : graphResolver.getLifecycles()) {
			log.info("Lifecycle " + lifecycle.getName() + ":");
			lifecycle.getContext().getModuleEdge().getTarget()
					.visit(new LoggingVisitor(lifecycle.getContext().getModuleEdge().getExecutionType(), context, 0, "context"));
			lifecycle.getProcessors().forEach(processor -> {
				try {
					processor.getModuleEdge().getTarget().visit(new LoggingVisitor(processor.getModuleEdge().getExecutionType(), context, 0, "processor"));
				} catch (RhenaException e) {

					e.printStackTrace();
				}
			});
			lifecycle.getGenerator().getModuleEdge().getTarget()
					.visit(new LoggingVisitor(lifecycle.getGenerator().getModuleEdge().getExecutionType(), context, 0, "generator"));
		}
		log.info("Execution plan: ");
		model.visit(new LoggingVisitor(type, context, 0, "entry"));

		log.info("Finished");
	}

}
