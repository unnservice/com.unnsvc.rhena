package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;

public interface IModelBuilder {

	public IRhenaExecution buildEntryPoint(ICaller caller) throws RhenaException;

}
