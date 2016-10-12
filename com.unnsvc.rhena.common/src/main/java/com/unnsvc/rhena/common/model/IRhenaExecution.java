package com.unnsvc.rhena.common.model;

import java.net.URL;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaExecution {

	public URL getArtifactURL() throws RhenaException;

}
