
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

public class Resource implements IResource {

	private EResourceType type;
	private File srcPath;

	public Resource(EResourceType type, File srcPath) {

		this.type = type;
		this.srcPath = srcPath;
	}

	@Override
	public EResourceType getResourceType() {

		return type;
	}

	@Override
	public File getSrcPath() {

		return srcPath;
	}
}
