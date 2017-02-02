
package com.unnsvc.rhena.common.execution;

import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class ExplodedArtifact extends AbstractArtifact implements IExplodedArtifact {

	private static final long serialVersionUID = 1L;

	public ExplodedArtifact(String name, URL artifactUrl) {

		super(name, artifactUrl);
	}

	public ExplodedArtifact(String name, String artifactUrl) throws RhenaException {

		super(name, artifactUrl);
	}

}
