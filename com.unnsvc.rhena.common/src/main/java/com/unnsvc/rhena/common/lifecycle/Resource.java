
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;

public class Resource implements IResource {

	private static final long serialVersionUID = 1L;
	private EResourceType type;
	private String relativePath;
	private File absolutePath;

	public Resource(EResourceType type, String relativePath, File absolutePath) {

		this.type = type;
		this.relativePath = relativePath;
		this.absolutePath = absolutePath;
	}

	@Override
	public EResourceType getResourceType() {

		return type;
	}

	@Override
	public String getRelativePath() {

		return relativePath;
	}

	@Override
	public File getAbsolutePath() {

		return absolutePath;
	}
}
