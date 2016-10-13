package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.execution.ExecutionType;

public interface IRhenaEdge {

	public ExecutionType getExecutionType();

	public IRhenaModule getTarget();

	public void setTarget(IRhenaModule target);

	public TraverseType getTraverseType();

}
