
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.EExecutionType;

public interface IExecutionEdge extends Runnable {

	public IExecutionModule getSource();

	public EExecutionType getType();

	public IExecutionModule getTarget();

}
