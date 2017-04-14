
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public abstract class AbstractExecution implements IRhenaExecution {

	private static final long serialVersionUID = 1L;
	private ModuleIdentifier moduleIdentifier;
	private EExecutionType executionType;
	private List<IArtifactDescriptor> artifacts;
	private Calendar executionDate;

	public AbstractExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts, Calendar executionDate) {

		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifacts = artifacts;
		this.executionDate = executionDate;
	}

	public AbstractExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts) {

		this(moduleIdentifier, executionType, artifacts, Calendar.getInstance());
	}

	@Override
	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public List<IArtifactDescriptor> getArtifacts() {

		return artifacts;
	}

	@Override
	public Calendar getExecutionDate() {

		return executionDate;
	}

	@Override
	public EExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifacts == null) ? 0 : artifacts.hashCode());
		result = prime * result + ((executionDate == null) ? 0 : executionDate.hashCode());
		result = prime * result + ((executionType == null) ? 0 : executionType.hashCode());
		result = prime * result + ((moduleIdentifier == null) ? 0 : moduleIdentifier.hashCode());
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
		AbstractExecution other = (AbstractExecution) obj;
		if (artifacts == null) {
			if (other.artifacts != null)
				return false;
		} else if (!artifacts.equals(other.artifacts))
			return false;
		if (executionDate == null) {
			if (other.executionDate != null)
				return false;
		} else if (!executionDate.equals(other.executionDate))
			return false;
		if (executionType != other.executionType)
			return false;
		if (moduleIdentifier == null) {
			if (other.moduleIdentifier != null)
				return false;
		} else if (!moduleIdentifier.equals(other.moduleIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "AbstractExecution [moduleIdentifier=" + moduleIdentifier + ", executionType=" + executionType + ", artifacts=" + artifacts + ", executionDate="
				+ executionDate + "]";
	}

}
