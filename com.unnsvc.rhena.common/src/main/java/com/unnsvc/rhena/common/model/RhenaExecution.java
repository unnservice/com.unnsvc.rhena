
package com.unnsvc.rhena.common.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaExecution {

	private ModuleIdentifier moduleIdentifier;
	private ExecutionType edgeType;
	private File artifact;

	public RhenaExecution(ModuleIdentifier moduleIdentifier, ExecutionType edgeType, File artifact) {

		this.moduleIdentifier = moduleIdentifier;
		this.edgeType = edgeType;
		this.artifact = artifact;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public ExecutionType getEdgeType() {

		return edgeType;
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
		result = prime * result + ((edgeType == null) ? 0 : edgeType.hashCode());
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
		if (edgeType != other.edgeType)
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

		return "RhenaExecution [moduleIdentifier=" + moduleIdentifier + ", edgeType=" + edgeType + ", artifact=" + artifact + "]";
	}

}
