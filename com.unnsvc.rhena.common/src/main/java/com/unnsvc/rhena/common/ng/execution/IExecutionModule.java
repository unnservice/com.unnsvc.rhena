
package com.unnsvc.rhena.common.ng.execution;

public interface IExecutionModule {

	public boolean isBuildable();

	public void removeExecuted(IExecutionEdge edge);

}
