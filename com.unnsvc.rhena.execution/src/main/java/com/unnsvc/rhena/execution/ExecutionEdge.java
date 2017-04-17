
package com.unnsvc.rhena.execution;

import com.unnsvc.rhena.common.ng.execution.IExecutionEdge;
import com.unnsvc.rhena.common.ng.execution.IExecutionModule;
import com.unnsvc.rhena.common.ng.model.EExecutionType;

public class ExecutionEdge implements IExecutionEdge {

	private IExecutionModule source;
	private EExecutionType type;
	private IExecutionModule target;

	public ExecutionEdge(IExecutionModule source, EExecutionType type, IExecutionModule target) {

		this.source = source;
		this.type = type;
		this.target = target;
	}

	@Override
	public void run() {
		
		// collect dependencies from target
		// submit target to agent will all exeuction information
		// removal from of edge from source
		source.removeExecuted(this);
		
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IExecutionModule getSource() {

		return source;
	}

	@Override
	public EExecutionType getType() {

		return type;
	}

	@Override
	public IExecutionModule getTarget() {

		return target;
	}
}
