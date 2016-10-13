
package com.unnsvc.rhena.core.execution;

public class ExecutionDescriptor {

	private ArtifactDescriptor artifact;

	public ExecutionDescriptor(ArtifactDescriptor artifact) {

		this.artifact = artifact;
	}

	public ArtifactDescriptor getArtifact() {

		return artifact;
	}
}
