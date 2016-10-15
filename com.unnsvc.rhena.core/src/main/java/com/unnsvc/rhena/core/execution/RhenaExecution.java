
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public class RhenaExecution implements IRhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private IExecutionType executionType;
	private IArtifactDescriptor artifact;
	private Calendar executionDate;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, IExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate) {

		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifact = artifact;
		this.executionDate = executionDate;
	}

	public RhenaExecution(ModuleIdentifier moduleIdentifier, IExecutionType executionType, IArtifactDescriptor artifact) {

		this(moduleIdentifier, executionType, artifact, Calendar.getInstance());
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public IExecutionType getExecutionType() {

		return executionType;
	}

	@Override
	public IArtifactDescriptor getArtifact() {

		return artifact;
	}

	public Calendar getExecutionDate() {

		return executionDate;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
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
		RhenaExecution other = (RhenaExecution) obj;
		if (artifact == null) {
			if (other.artifact != null)
				return false;
		} else if (!artifact.equals(other.artifact))
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

		return "RhenaExecution [moduleIdentifier=" + moduleIdentifier + ", executionType=" + executionType + ", artifact=" + artifact + "]";
	}

}
