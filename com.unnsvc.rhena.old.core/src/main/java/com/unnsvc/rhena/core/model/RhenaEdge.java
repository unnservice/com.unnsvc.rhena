
package com.unnsvc.rhena.core.model;

import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;

public class RhenaEdge implements IRhenaEdge {

	private static final long serialVersionUID = 1L;
	private ModuleIdentifier source;
	private IEntryPoint entryPoint;
	private ESelectionType traverseType;

	public RhenaEdge(ModuleIdentifier source, IEntryPoint entryPoint, ESelectionType traverseType) {

		this.source = source;
		this.entryPoint = entryPoint;
		this.traverseType = traverseType;
	}

	@Override
	public ModuleIdentifier getSource() {

		return source;
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
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (traverseType != other.traverseType)
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "RhenaEdge [source=" + source + ", entryPoint=" + entryPoint + ", traverseType=" + traverseType + "]";
	}
}
