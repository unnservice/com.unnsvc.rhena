package com.unnsvc.rhena.common.ng.execution;

import com.unnsvc.rhena.common.ng.model.EExecutionType;

public interface IExecutionEdge {

	public IExecutionModule getSource();

	public EExecutionType getType();

	public IExecutionModule getTarget();

}
