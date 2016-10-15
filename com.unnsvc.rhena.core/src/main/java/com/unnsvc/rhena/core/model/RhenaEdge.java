
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

	/**
	 * Instead of deep hash on target, we use target.getModuleIdentifier
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((executionType == null) ? 0 : executionType.hashCode());
		result = prime * result + ((target.getModuleIdentifier() == null) ? 0 : target.getModuleIdentifier().hashCode());
		result = prime * result + ((traverseType == null) ? 0 : traverseType.hashCode());
		return result;
	}

	/**
	 * Instead of deep-check on target, we use target.getModuleIdentifier
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RhenaEdge other = (RhenaEdge) obj;
		if (executionType != other.executionType)
			return false;
		if (target.getModuleIdentifier() == null) {
			if (other.target.getModuleIdentifier() != null)
				return false;
		} else if (!target.getModuleIdentifier().equals(other.target.getModuleIdentifier()))
			return false;
		if (traverseType != other.traverseType)
			return false;
		return true;
	}
}
