
package com.unnsvc.rhena.core.execution;

import java.util.concurrent.Callable;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.logging.IRhenaLogger;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class ExecutingCallable implements Callable<ExecutionResult> {

	private IRhenaLogger log;
	private IRhenaContext context;
	private IRhenaEdge edge;

	public ExecutingCallable(IRhenaContext context, IRhenaEdge edge) {

		this.context = context;
		this.edge = edge;
		this.log = context.getLogger(getClass());
	}

	@Override
	public ExecutionResult call() throws Exception {

		log.info(edge.getTarget().getModuleIdentifier(), edge.getExecutionType(), "Executing");

		// produce executed model state

		// @TODO materialiseExecution should either throw exception or
		// return executable, but never return null
		IRhenaExecution execution = context.materialiseExecution(edge.getTarget(), edge.getExecutionType());

		return new ExecutionResult(edge, execution);
	}

	public IRhenaEdge getEdge() {

		return edge;
	}

}
