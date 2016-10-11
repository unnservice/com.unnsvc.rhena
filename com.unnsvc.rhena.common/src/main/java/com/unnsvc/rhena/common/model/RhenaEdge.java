
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private ExecutionType executionType;
	private RhenaModule target;

	public RhenaEdge(ExecutionType executionType, RhenaModule target) {

		this.executionType = executionType;
		this.target = target;
	}

	public ExecutionType getExecutionType() {

		return executionType;
	}

	public RhenaModule getTarget() {

		return target;
	}

	public void setTarget(RhenaModule target) {

		this.target = target;
	}
}
