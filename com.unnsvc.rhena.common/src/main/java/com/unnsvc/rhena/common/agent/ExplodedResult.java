
package com.unnsvc.rhena.common.agent;

import java.net.URL;

import com.unnsvc.rhena.common.execution.IExplodedResult;

public class ExplodedResult extends ArtifactResult implements IExplodedResult {

	private static final long serialVersionUID = 1L;
	private URL sourceUrl;

	public ExplodedResult(String name, URL resultUrl, URL sourceUrl) {

		super(name, resultUrl);
		this.sourceUrl = sourceUrl;
	}

	@Override
	public URL getSourceUrl() {

		return sourceUrl;
	}
}
