
package com.unnsvc.rhena.lifecycle.paths;

import java.io.File;

public class Resource {

	private EResourceType type;
	private File srcPath;

	public Resource(EResourceType type, File srcPath) {

		this.type = type;
		this.srcPath = srcPath;
	}

	public EResourceType getType() {

		return type;
	}

	public File getSrcPath() {

		return srcPath;
	}
}
