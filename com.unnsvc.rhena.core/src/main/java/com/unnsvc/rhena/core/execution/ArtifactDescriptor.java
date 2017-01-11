
package com.unnsvc.rhena.core.execution;

import java.net.MalformedURLException;
import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;

public class ArtifactDescriptor implements IArtifactDescriptor {

	private static final long serialVersionUID = 1L;
	private String name;
	private URL artifactUrl;
	private String sha1;

	public ArtifactDescriptor(String name, URL artifactUrl, String sha1) {

		this.name = name;
		this.artifactUrl = artifactUrl;
		this.sha1 = sha1;
	}

	public ArtifactDescriptor(String name, String artifactUrl, String sha1) throws RhenaException {

		this.name = name;
		try {
			this.artifactUrl = new URL(artifactUrl);
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
		this.sha1 = sha1;
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
	public String getSha1() {

		return sha1;
	}

	@Override
	public String toString() {

		return "ArtifactDescriptor [name=" + name + ", artifactUrl=" + artifactUrl + ", sha1=" + sha1 + "]";
	}
}
