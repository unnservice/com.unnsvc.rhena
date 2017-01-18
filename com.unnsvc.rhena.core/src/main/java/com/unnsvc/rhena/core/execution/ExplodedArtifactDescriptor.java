
package com.unnsvc.rhena.core.execution;

import java.net.URL;

import com.unnsvc.rhena.common.execution.IExplodedArtifactDescriptor;

public class ExplodedArtifactDescriptor extends AbstractArtifactDescriptor implements IExplodedArtifactDescriptor {

	private static final long serialVersionUID = 1L;
	private URL artifactSourceUrl;

	public ExplodedArtifactDescriptor(String name, URL artifactUrl, URL artifactSourceUrl) {

		super(name, artifactUrl);
		this.artifactSourceUrl = artifactSourceUrl;
	}

	@Override
	public URL getArtifactSourceUrl() {

		return artifactSourceUrl;
	}
}
