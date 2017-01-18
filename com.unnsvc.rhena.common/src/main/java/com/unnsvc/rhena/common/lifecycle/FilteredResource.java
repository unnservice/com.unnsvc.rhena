
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;

public class FilteredResource extends Resource implements IFilteredResource {

	private static final long serialVersionUID = 1L;
	private String relativeIntermediaryPath;

	public FilteredResource(EExecutionType type, File baseDir, String relativeSourcePath, String relativeOutputPath, String relativeIntermediaryPath)
			throws RhenaException {

		super(type, baseDir, relativeSourcePath, relativeOutputPath);
		this.relativeIntermediaryPath = relativeIntermediaryPath;
	}

	@Override
	public String getRelativeIntermediaryPath() {

		return relativeIntermediaryPath;
	}

	@Override
	public String toString() {

		return "FilteredResource [relativeIntermediaryPath=" + relativeIntermediaryPath + ", getResourceType()=" + getResourceType()
				+ ", getRelativeSourcePath()=" + getRelativeSourcePath() + ", getRelativeOutputPath()=" + getRelativeOutputPath() + ", getBaseDirectory()="
				+ getBaseDirectory() + "]";
	}
}
