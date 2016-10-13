
package com.unnsvc.rhena.core.execution;

import java.net.URL;

import com.unnsvc.rhena.common.execution.IArtifactDescriptor;

public class ArtifactDescriptor implements IArtifactDescriptor {

	private String name;
	private URL artifactUrl;
	private String sha1;

	public ArtifactDescriptor(String name, URL artifactUrl, String sha1) {

		this.name = name;
		this.artifactUrl = artifactUrl;
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
