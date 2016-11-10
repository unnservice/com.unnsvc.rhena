
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class RhenaEdge implements IRhenaEdge {

	private IEntryPoint entryPoint;
	private ESelectionType traverseType;

	public RhenaEdge(IEntryPoint entryPoint, ESelectionType traverseType) {

		this.entryPoint = entryPoint;
		this.traverseType = traverseType;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	@Override
	public ESelectionType getTraverseType() {

		return traverseType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryPoint == null) ? 0 : entryPoint.hashCode());
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
		if (entryPoint == null) {
			if (other.entryPoint != null)
				return false;
		} else if (!entryPoint.equals(other.entryPoint))
			return false;
		if (traverseType != other.traverseType)
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "RhenaEdge [entryPoint=" + entryPoint + ", traverseType=" + traverseType + "]";
	}

}
