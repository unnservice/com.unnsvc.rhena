
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public class RhenaEdge implements IRhenaEdge {

	private IExecutionType executionType;
	private IRhenaModule target;
	private TraverseType traverseType;

	public RhenaEdge(IExecutionType executionType, IRhenaModule target, TraverseType traverseType) {
		
		this.executionType = executionType;
		this.target = target;
		this.traverseType = traverseType;
	}

	@Override
	public IExecutionType getExecutionType() {

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
