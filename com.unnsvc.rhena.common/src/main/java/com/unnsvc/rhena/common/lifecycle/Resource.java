
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.execution.EExecutionType;

public class Resource implements IResource {

	private static final long serialVersionUID = 1L;
	private EExecutionType type;
	private File baseDir;
	private String relativeSourcePath;
	private String relativeOutputPath;
	private String originalRelativeSourcePath;

	public Resource(EExecutionType type, File baseDir, String relativeSourcePath, String relativeOutputPath) {

		this.type = type;
		this.baseDir = baseDir;
		this.relativeSourcePath = relativeSourcePath;
		this.relativeOutputPath = relativeOutputPath;
	}
	
	public Resource(EExecutionType type, File baseDir, String relativeSourcePath, String relativeOutputPath, String originalRelativeSourcePath) {

		this(type, baseDir, relativeSourcePath, relativeOutputPath);
		this.originalRelativeSourcePath = originalRelativeSourcePath;
	}

	@Override
	public EExecutionType getResourceType() {

		return type;
	}

	@Override
	public String getRelativeSourcePath() {

		return relativeSourcePath;
	}

	@Override
	public String getRelativeOutputPath() {

		return relativeOutputPath;
	}

	@Override
	public File getBaseDirectory() {

		return baseDir;
	}
	
	@Override
	public String getOriginalRelativeSourcePath() {
		
		if(originalRelativeSourcePath == null) {
			return getRelativeSourcePath();
		}
		return originalRelativeSourcePath;
	}

	@Override
	public String toString() {

		return "Resource [type=" + type + ", baseDir=" + baseDir + ", relativeSourcePath=" + relativeSourcePath + ", relativeOutputPath=" + relativeOutputPath
				+ "]";
	}
}
