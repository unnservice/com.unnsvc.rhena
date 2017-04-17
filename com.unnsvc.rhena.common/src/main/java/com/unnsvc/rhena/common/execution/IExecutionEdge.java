
package com.unnsvc.rhena.common.execution;

import java.util.concurrent.Callable;

import com.unnsvc.rhena.common.model.EExecutionType;

public interface IExecutionEdge extends Callable<Object> {

	public IExecutionModule getSource();

	public EExecutionType getType();

	public IExecutionModule getTarget();

}
