
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;
import java.net.URL;

public interface IArtifactDescriptor extends Serializable {

	public static final String DEFAULT = "default";
	public static final String SOURCES = "sources";

	public String getClassifier();

	public String getArtifactName();

	public URL getArtifactUrl();

}
