
package com.unnsvc.rhena.core.model;

import java.net.URL;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;

public class RhenaExecution implements IRhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private ExecutionType executionType;
	private URL artifact;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, ExecutionType executionType, URL artifact) {

		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifact = artifact;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public ExecutionType getExecutionType() {

		return executionType;
	}

	public URL getArtifact() {

		return artifact;
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
