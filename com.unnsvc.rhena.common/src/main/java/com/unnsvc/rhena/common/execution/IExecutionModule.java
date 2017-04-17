
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IExecutionModule {

	public boolean isBuildable();

	public void removeExecuted(IExecutionEdge edge);

	public IRhenaModule getModule();

}
