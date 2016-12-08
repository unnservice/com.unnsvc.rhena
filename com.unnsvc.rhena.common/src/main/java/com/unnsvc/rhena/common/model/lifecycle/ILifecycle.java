package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IEntryPoint;

public interface ILifecycle {

	public IRhenaExecution executeModel(IEntryPoint entryPoint, Map<IRhenaExecution, List<IRhenaExecution>> depchains);

}
