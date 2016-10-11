package com.unnsvc.rhena.common.model;


public interface IRhenaEdge {

	public ExecutionType getExecutionType();

	public IRhenaModule getTarget();

	public void setTarget(IRhenaModule target);

	public TraverseType getTraverseType();

}
