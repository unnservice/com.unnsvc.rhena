
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;
import java.io.Serializable;

import com.unnsvc.rhena.common.execution.EExecutionType;

public interface IResource extends Serializable {

	public EExecutionType getResourceType();

	public File getBaseDirectory();
	
	public String getRelativeSourcePath();

	public String getRelativeOutputPath();

}
