
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IExecutionResult {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

}
