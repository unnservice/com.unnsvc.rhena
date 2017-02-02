
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;
import java.net.URL;

public interface IArtifact extends Serializable {

	public String getArtifactName();

	public URL getArtifactUrl();

}
