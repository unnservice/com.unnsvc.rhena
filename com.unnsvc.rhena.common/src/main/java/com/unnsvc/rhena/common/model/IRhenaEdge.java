package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public interface IRhenaEdge {

	public IExecutionType getExecutionType();

	public IRhenaModule getTarget();

	public void setTarget(IRhenaModule target);

	public TraverseType getTraverseType();

}
