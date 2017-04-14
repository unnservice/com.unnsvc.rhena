
package com.unnsvc.rhena.core.execution;

import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class ExecutionResult {

	private IRhenaEdge edge;
	private IRhenaExecution execution;

	public ExecutionResult(IRhenaEdge edge, IRhenaExecution execution) {

		this.edge = edge;
		this.execution = execution;
	}

	public IRhenaEdge getEdge() {

		return edge;
	}

	public IRhenaExecution getExecution() {

		return execution;
	}
}
