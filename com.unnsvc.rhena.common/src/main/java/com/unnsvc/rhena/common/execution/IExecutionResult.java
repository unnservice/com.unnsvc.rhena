
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IExecutionResult {

	public EExecutionType getType();

	public IRhenaModule getModule();

}
