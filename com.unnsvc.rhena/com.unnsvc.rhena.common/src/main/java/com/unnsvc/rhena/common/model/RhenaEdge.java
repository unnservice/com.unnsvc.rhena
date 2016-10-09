
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private RhenaEdgeType dependencyType;
	private ModuleIdentifier target;

	public RhenaEdge(RhenaEdgeType dependencyType, ModuleIdentifier target) {

		this.dependencyType = dependencyType;
		this.target = target;
	}

	public RhenaEdgeType getDependencyType() {

		return dependencyType;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependencyType == null) ? 0 : dependencyType.hashCode());
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
		if (dependencyType != other.dependencyType)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

}
