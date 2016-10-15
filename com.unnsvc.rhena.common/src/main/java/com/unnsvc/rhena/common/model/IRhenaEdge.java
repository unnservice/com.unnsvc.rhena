package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.execution.EExecutionType;

public interface IRhenaEdge {

	public EExecutionType getExecutionType();

	public IRhenaModule getTarget();

	public void setTarget(IRhenaModule target);

	public TraverseType getTraverseType();

}
