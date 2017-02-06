
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;

/**
 * @author noname
 *
 */
public interface IArtifactDescriptor extends Serializable {

	public static final String DEFAULT_CLASSIFIER = "default";

	public String getClassifier();

	public IArtifact getPrimaryArtifact();

	/**
	 * Sources artifact, possibly null
	 * 
	 * @return
	 */
	public IArtifact getSourceArtifact();

	/**
	 * Javadoc artifact, possibly null
	 * 
	 * @return
	 */
	public IArtifact getJavadocArtifact();

}
