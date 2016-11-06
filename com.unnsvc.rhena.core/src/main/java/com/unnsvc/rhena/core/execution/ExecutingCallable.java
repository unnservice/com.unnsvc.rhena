
package com.unnsvc.rhena.core.execution;

import java.util.concurrent.Callable;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutingCallable implements Callable<ExecutionResult> {

	private IRhenaLoggingHandler log;
	private IRhenaContext context;
	private IRhenaEdge edge;

	public ExecutingCallable(IRhenaContext context, IRhenaEdge edge) {

		this.context = context;
		this.edge = edge;
		this.log = context.getLogger(getClass());
	}

	@Override
	public ExecutionResult call() throws Exception {

		IRhenaModule module  = context.materialiseModel(edge.getTarget());
		
		log.info(edge.getTarget(), edge.getExecutionType(), "Executing");

		// produce executed model state

		// @TODO materialiseExecution should either throw exception or
		// return executable, but never return null
		IRhenaExecution execution = context.materialiseExecution(module, edge.getExecutionType());

		return new ExecutionResult(edge, execution);
	}

	public IRhenaEdge getEdge() {

		return edge;
	}

}
