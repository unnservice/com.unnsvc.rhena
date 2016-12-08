
package com.unnsvc.rhena.core.lifecycle;

import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;

public class Lifecycle implements ILifecycle {

	public Lifecycle() {

	}

	@Override
	public IRhenaExecution executeModel(IEntryPoint entryPoint, Map<IRhenaExecution, List<IRhenaExecution>> depchains) {
		
		throw new UnsupportedOperationException("Not implemented");
	}
}
