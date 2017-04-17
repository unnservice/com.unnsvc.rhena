
package com.unnsvc.rhena.core.treewalk;

import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class FlatTreeFrame {

	private IEntryPoint entryPoint;
	private ESelectionType selectionType;
	private ESelectionType previousSelectionType;

	public FlatTreeFrame(IEntryPoint entryPoint, ESelectionType selectionType) {

		this(entryPoint, selectionType, ESelectionType.SCOPE);
	}

	public FlatTreeFrame(IEntryPoint entryPoint, ESelectionType selectionType, ESelectionType previousSelectionType) {

		this.entryPoint = entryPoint;
		this.selectionType = selectionType;
		this.previousSelectionType = previousSelectionType;
	}

	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	public ESelectionType getSelectionType() {

		return selectionType;
	}

	public ESelectionType getPreviousSelectionType() {

		return previousSelectionType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryPoint == null) ? 0 : entryPoint.hashCode());
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
		FlatTreeFrame other = (FlatTreeFrame) obj;
		if (entryPoint == null) {
			if (other.entryPoint != null)
				return false;
		} else if (!entryPoint.equals(other.entryPoint))
			return false;
		return true;
	}

}
