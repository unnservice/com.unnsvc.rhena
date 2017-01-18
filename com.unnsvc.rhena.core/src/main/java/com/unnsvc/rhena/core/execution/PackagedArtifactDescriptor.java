
package com.unnsvc.rhena.core.execution;

import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IPackagedArtifactDescriptor;

public class PackagedArtifactDescriptor extends AbstractArtifactDescriptor implements IPackagedArtifactDescriptor {

	private static final long serialVersionUID = 1L;

	private String sha1;

	public PackagedArtifactDescriptor(String name, URL artifactUrl, String sha1) {

		super(name, artifactUrl);
		this.sha1 = sha1;
	}

	public PackagedArtifactDescriptor(String name, String artifactUrl, String sha1) throws RhenaException {

		super(name, artifactUrl);
		this.sha1 = sha1;
	}

	@Override
	public String getSha1() {

		return sha1;
	}

	@Override
	public String toString() {

		return "PackagedArtifactDescriptor [sha1=" + sha1 + ", getArtifactName()=" + getArtifactName() + ", getArtifactUrl()=" + getArtifactUrl() + "]";
	}
}
