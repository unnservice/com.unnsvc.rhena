
package com.unnsvc.rhena.lifecycle.resource;

import java.io.File;

import com.unnsvc.rhena.common.model.lifecycle.IResource;

/**
 * @TODO implement include/exclude filters
 * @author noname
 *
 */
public class Resource implements IResource {

	private File projectBase;
	private String sourcePath;
	private String targetPath;

	/**
	 * @TODO evaluate potential framework support in staging "this.staging"
	 */

	public Resource(File projectBase, String sourcePath, String targetPath) {

		this.projectBase = new File(projectBase.toURI().normalize().getPath());
		this.sourcePath = sourcePath;
		this.targetPath = targetPath;
	}

	@Override
	public File getProjectBase() {

		return projectBase;
	}

	@Override
	public String getSourcePath() {

		return sourcePath;
	}

	@Override
	public String getTargetPath() {

		return targetPath;
	}

	@Override
	public File getSourceFile() {

		return new File(getProjectBase(), getSourcePath());
	}

	@Override
	public File getTargetFile() {

		return new File(getProjectBase(), getTargetPath());
	}

	@Override
	public String toString() {

		return "Resource [projectBase=" + projectBase + ", sourcePath=" + sourcePath + ", targetPath=" + targetPath + "]";
	}
}
