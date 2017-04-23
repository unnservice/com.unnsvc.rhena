
package com.unnsvc.rhena.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class EntryPoint implements IEntryPoint {

	private static final long serialVersionUID = 1L;
	private EExecutionType executionType;
	private ModuleIdentifier target;

	public EntryPoint(EExecutionType executionType, ModuleIdentifier target) {

		this.executionType = executionType;
		this.target = target;
	}

	@Override
	public EExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public void setExecutionType(EExecutionType executionType) {

		this.executionType = executionType;
	}

	@Override
	public ModuleIdentifier getTarget() {

		return target;
	}

	@Override
	public void setTarget(ModuleIdentifier target) {

		this.target = target;
	}

	@Override
	public String toString() {

		return "EntryPoint [executionType=" + executionType + ", target=" + target + "]";
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
		EntryPoint other = (EntryPoint) obj;
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
