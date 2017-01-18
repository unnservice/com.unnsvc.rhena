
package com.unnsvc.rhena.common.agent;

import java.net.URL;

import com.unnsvc.rhena.common.execution.IResult;

public class ArtifactResult implements IResult {

	private URL resultUrl;
	private String name;

	public ArtifactResult(String name, URL resultUrl) {

		this.name = name;
		this.resultUrl = resultUrl;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public URL getResultUrl() {

		return resultUrl;
	}

}
