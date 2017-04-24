
package com.unnsvc.rhena.lifecycle.artifact;

import java.net.URL;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.artifact.IArtifact;

public class Artifact implements IArtifact {

	private URL location;
	private String[] tags;

	public Artifact(URL location, String[] tags) {

		this.location = location;
		this.tags = tags;
	}

	public Artifact(URL location) {

		this(location, new String[] { RhenaConstants.DEFAULT_ARTIFACT_TAG });
	}

	@Override
	public URL getLocation() {

		return location;
	}

	@Override
	public String[] getTags() {

		return tags;
	}
}
