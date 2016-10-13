
package com.unnsvc.rhena.common.execution;

import java.net.URL;

public interface IArtifactDescriptor {

	public String getArtifactName();

	public URL getArtifactUrl();

	public String getSha1();

}
