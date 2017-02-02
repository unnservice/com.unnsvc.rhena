package com.unnsvc.rhena.common.execution;

import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class ExplodedArtifactDescriptor extends AbstractArtifactDescriptor implements IExplodedArtifactDescriptor {

	private static final long serialVersionUID = 1L;

	public ExplodedArtifactDescriptor(String classifier, String name, URL artifactUrl) {

		super(classifier, name, artifactUrl);
	}

	public ExplodedArtifactDescriptor(String classifier, String name, String artifactUrl) throws RhenaException {

		super(classifier, name, artifactUrl);
	}

}
