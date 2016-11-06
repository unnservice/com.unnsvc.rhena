
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.TraverseType;

public class RhenaEdge implements IRhenaEdge {

	private EExecutionType executionType;
	private ModuleIdentifier target;
	private TraverseType traverseType;

	public RhenaEdge(EExecutionType executionType, ModuleIdentifier target, TraverseType traverseType) {

		this.executionType = executionType;
		this.target = target;
		this.traverseType = traverseType;
	}

	@Override
	public EExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public ModuleIdentifier getTarget() {

		return target;
	}

	@Override
	public TraverseType getTraverseType() {

		return traverseType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((executionType == null) ? 0 : executionType.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((traverseType == null) ? 0 : traverseType.hashCode());
		return result;
	}

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
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (traverseType != other.traverseType)
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "RhenaEdge [executionType=" + executionType + ", target=" + target + ", traverseType=" + traverseType + "]";
	}

}
