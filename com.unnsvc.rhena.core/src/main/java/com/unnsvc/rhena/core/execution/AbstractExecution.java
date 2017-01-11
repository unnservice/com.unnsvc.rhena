
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public abstract class AbstractExecution implements IRhenaExecution {

	private static final long serialVersionUID = 1L;
	private ModuleIdentifier moduleIdentifier;
	private EExecutionType executionType;
	private IArtifactDescriptor artifact;
	private Calendar executionDate;

	public AbstractExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate) {

		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifact = artifact;
		this.executionDate = executionDate;
	}

	public AbstractExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact) {

		this(moduleIdentifier, executionType, artifact, Calendar.getInstance());
	}

	@Override
	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public IArtifactDescriptor getArtifact() {

		return artifact;
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
		result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
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
		IRhenaExecution other = (IRhenaExecution) obj;
		if (artifact == null) {
			if (other.getArtifact() != null)
				return false;
		} else if (!artifact.equals(other.getArtifact()))
			return false;
		if (executionDate == null) {
			if (other.getExecutionDate() != null)
				return false;
		} else if (!executionDate.equals(other.getExecutionDate()))
			return false;
		if (executionType != other.getExecutionType())
			return false;
		if (moduleIdentifier == null) {
			if (other.getModuleIdentifier() != null)
				return false;
		} else if (!moduleIdentifier.equals(other.getModuleIdentifier()))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " [moduleIdentifier=" + moduleIdentifier + ", executionType=" + executionType + ", artifact=" + artifact
				+ ", executionDate=" + executionDate.getTime() + "]";
	}
}
