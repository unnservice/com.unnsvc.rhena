
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;

public interface IArtifactDescriptor extends Serializable {

	public static final String DEFAULT_CLASSIFIER = "default";
	
	public String getClassifier();

	public IArtifact getPrimaryArtifact();

	public IArtifact getSourceArtifact();

}
