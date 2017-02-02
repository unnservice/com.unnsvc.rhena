
package com.unnsvc.rhena.common.execution;

import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class PackagedArtifact extends AbstractArtifact implements IPackagedArtifact {

	private static final long serialVersionUID = 1L;

	private String sha1;

	public PackagedArtifact(String name, URL artifactUrl, String sha1) {

		super(name, artifactUrl);
		this.sha1 = sha1;
	}

	public PackagedArtifact(String name, String artifactUrl, String sha1) throws RhenaException {

		super(name, artifactUrl);
		this.sha1 = sha1;
	}

	@Override
	public String getSha1() {

		return sha1;
	}

	@Override
	public String toString() {

		return "PackagedArtifactDescriptor [sha1=" + sha1 + "]";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((sha1 == null) ? 0 : sha1.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackagedArtifact other = (PackagedArtifact) obj;
		if (sha1 == null) {
			if (other.sha1 != null)
				return false;
		} else if (!sha1.equals(other.sha1))
			return false;
		return true;
	}

}
