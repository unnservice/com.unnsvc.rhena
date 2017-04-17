
package com.unnsvc.rhena.common.ng.execution;

import com.unnsvc.rhena.common.ng.model.EExecutionType;

public interface IExecutionEdge extends Runnable {

	public IExecutionModule getSource();

	public EExecutionType getType();

	public IExecutionModule getTarget();

}
