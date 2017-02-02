
package com.unnsvc.rhena.common.execution;

public class ArtifactDescriptor implements IArtifactDescriptor {

	private static final long serialVersionUID = 1L;
	private String classifier;
	private IArtifact primaryArtifact;
	private IArtifact sourceArtifact;

	public ArtifactDescriptor(String classifier, IArtifact primaryArtifact, IArtifact sourceArtifact) {

		this.classifier = classifier;
		this.primaryArtifact = primaryArtifact;
		this.sourceArtifact = sourceArtifact;
	}

	public ArtifactDescriptor(String classifier, IArtifact primaryArtifact) {

		this(classifier, primaryArtifact, null);
	}

	@Override
	public String getClassifier() {

		return classifier;
	}

	@Override
	public IArtifact getPrimaryArtifact() {

		return primaryArtifact;
	}

	@Override
	public IArtifact getSourceArtifact() {

		return sourceArtifact;
	}
}
