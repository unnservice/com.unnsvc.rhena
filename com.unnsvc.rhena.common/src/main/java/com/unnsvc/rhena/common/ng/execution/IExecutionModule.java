
package com.unnsvc.rhena.common.ng.execution;

import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public interface IExecutionModule {

	public boolean isBuildable();

	public void removeExecuted(IExecutionEdge edge);

	public IRhenaModule getModule();

}
