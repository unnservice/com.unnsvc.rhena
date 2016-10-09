
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private RhenaExecutionType executionType;
	private ModuleIdentifier target;

	public RhenaEdge(RhenaExecutionType executionType, ModuleIdentifier target) {

		this.executionType = executionType;
		this.target = target;
	}

	public RhenaExecutionType getExecutionType() {

		return executionType;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((executionType == null) ? 0 : executionType.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		return true;
	}

}
