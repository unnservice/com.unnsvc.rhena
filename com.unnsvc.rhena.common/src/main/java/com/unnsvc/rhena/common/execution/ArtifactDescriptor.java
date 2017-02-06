
package com.unnsvc.rhena.common.execution;

public class ArtifactDescriptor implements IArtifactDescriptor {

	private static final long serialVersionUID = 1L;
	private String classifier;
	private IArtifact primaryArtifact;
	private IArtifact sourceArtifact;
	private IArtifact javadocArtifact;

	public ArtifactDescriptor(String classifier, IArtifact primaryArtifact, IArtifact sourceArtifact, IArtifact javadocArtifact) {

		this.classifier = classifier;
		this.primaryArtifact = primaryArtifact;
		this.sourceArtifact = sourceArtifact;
		this.javadocArtifact = javadocArtifact;
	}

	public ArtifactDescriptor(String classifier, IArtifact primaryArtifact) {

		this(classifier, primaryArtifact, null, null);
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

	@Override
	public IArtifact getJavadocArtifact() {

		return javadocArtifact;
	}
}
