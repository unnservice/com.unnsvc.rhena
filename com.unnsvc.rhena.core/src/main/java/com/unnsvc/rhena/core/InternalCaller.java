
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class InternalCaller extends Caller {

	private static final long serialVersionUID = 1L;

	public InternalCaller(IRhenaModule module, EExecutionType executionType) {

		super(module, executionType);
	}

	@Override
	public String toString() {

		return "InternalCaller [getEntryPoint()=" + getEntryPoint() + "]";
	}
}
