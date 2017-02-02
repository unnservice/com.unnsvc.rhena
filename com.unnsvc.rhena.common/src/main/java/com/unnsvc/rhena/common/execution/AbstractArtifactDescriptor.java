
package com.unnsvc.rhena.common.execution;

import java.net.MalformedURLException;
import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public abstract class AbstractArtifactDescriptor implements IArtifactDescriptor {

	private static final long serialVersionUID = 1L;

	private String classifier;
	private String name;
	private URL artifactUrl;

	public AbstractArtifactDescriptor(String classifier, String name, URL artifactUrl) {

		this.classifier = classifier;
		this.name = name;
		this.artifactUrl = artifactUrl;
	}

	public AbstractArtifactDescriptor(String classifier, String name, String artifactUrl) throws RhenaException {

		this.classifier = classifier;
		this.name = name;
		try {
			this.artifactUrl = new URL(artifactUrl);
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}
	
	@Override
	public String getClassifier() {

		return classifier;
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

}
