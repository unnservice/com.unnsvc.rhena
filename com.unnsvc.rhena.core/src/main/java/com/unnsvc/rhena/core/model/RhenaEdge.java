
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;

public class RhenaEdge implements IRhenaEdge {

	private EExecutionType executionType;
	private IRhenaModule target;
	private TraverseType traverseType;

	public RhenaEdge(EExecutionType executionType, IRhenaModule target, TraverseType traverseType) {
		
		this.executionType = executionType;
		this.target = target;
		this.traverseType = traverseType;
	}

	@Override
	public EExecutionType getExecutionType() {

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

	@Override
	public String toString() {

		return "RhenaEdge [executionType=" + executionType + ", target=" + target.getModuleIdentifier() + ", traverseType=" + traverseType + "]";
	}
}
