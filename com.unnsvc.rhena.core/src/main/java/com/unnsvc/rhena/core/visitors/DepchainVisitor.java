
package com.unnsvc.rhena.core.visitors;

import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class DepchainVisitor extends AbstractProceduralModelVisitor {

	private EExecutionType executionType;

	public DepchainVisitor(IModelResolver resolver, EExecutionType executionType) {

		super(resolver);
		this.executionType = executionType;
	}

	public Map<IRhenaExecution, List<IRhenaExecution>> getExecutions() {

		return null;
	}

	@Override
	public void startVisit(IRhenaModule module) {

	}

	@Override
	public void endVisit(IRhenaModule module) {

	}
}
