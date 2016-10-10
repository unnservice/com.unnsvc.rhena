
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private RhenaExecutionType executionType;
	private RhenaModule target;

	public RhenaEdge(RhenaExecutionType executionType, RhenaModule target) {

		this.executionType = executionType;
		this.target = target;
	}

	public RhenaExecutionType getExecutionType() {

		return executionType;
	}

	public RhenaModule getTarget() {

		return target;
	}

	public void setTarget(RhenaModule target) {

		this.target = target;
	}
}
