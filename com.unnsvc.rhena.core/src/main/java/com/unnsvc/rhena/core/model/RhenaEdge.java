
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;

public class RhenaEdge implements IRhenaEdge {

	private ExecutionType executionType;
	private IRhenaModule target;
	private TraverseType traverseType;

	public RhenaEdge(ExecutionType executionType, IRhenaModule target, TraverseType traverseType) {
		
		this.executionType = executionType;
		this.target = target;
		this.traverseType = TraverseType.NONE;
	}

	@Override
	public ExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public IRhenaModule getTarget() {

		return target;
	}

	@Override
	public void setTarget(IRhenaModule target) {

		this.target = target;
	}
	
	@Override
	public TraverseType getTraverseType() {

		return traverseType;
	}
}
