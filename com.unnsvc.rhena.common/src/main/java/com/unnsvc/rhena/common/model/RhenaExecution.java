
package com.unnsvc.rhena.common.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private ExecutionType executionType;
	private File artifact;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, ExecutionType executionType, File artifact) {

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

	public File getArtifact() {

		return artifact;
	}

	/**
	 * @TODO shouldn't do this here because its super ugly, keep URLs in the
	 *       execution type instead of File?
	 * @return
	 */
	public URL getArtifactURL() throws RhenaException {

		try {

			return artifact.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RhenaException("This shouldn't happen, please report a bug", e);
		}
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
