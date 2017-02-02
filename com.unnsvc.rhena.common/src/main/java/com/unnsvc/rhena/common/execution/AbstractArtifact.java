
package com.unnsvc.rhena.common.execution;

import java.net.MalformedURLException;
import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public abstract class AbstractArtifact implements IArtifact {

	private static final long serialVersionUID = 1L;

	private String name;
	private URL artifactUrl;

	public AbstractArtifact(String name, URL artifactUrl) {

		this.name = name;
		this.artifactUrl = artifactUrl;
	}

	public AbstractArtifact(String name, String artifactUrl) throws RhenaException {

		this.name = name;
		try {
			this.artifactUrl = new URL(artifactUrl);
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}

	@Override
	public String getArtifactName() {

		return name;
	}

	@Override
	public URL getArtifactUrl() {

		return artifactUrl;
	}

	@Override
	public String toString() {

		return "AbstractArtifactDescriptor [name=" + name + ", artifactUrl=" + artifactUrl + "]";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactUrl == null) ? 0 : artifactUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AbstractArtifact other = (AbstractArtifact) obj;
		if (artifactUrl == null) {
			if (other.artifactUrl != null)
				return false;
		} else if (!artifactUrl.equals(other.artifactUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
