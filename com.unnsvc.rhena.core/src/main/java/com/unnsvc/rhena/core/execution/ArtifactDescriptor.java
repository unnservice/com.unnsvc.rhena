
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

public class ArtifactDescriptor {

	private String artifact;
	private String sha1;
	private Calendar date;

	public ArtifactDescriptor(String artifact, String sha1, Calendar date) {

		this.artifact = artifact;
		this.sha1 = sha1;
		this.date = date;
	}

	public String getArtifact() {

		return artifact;
	}

	public Calendar getDate() {

		return date;
	}

	public String getSha1() {

		return sha1;
	}
}
