
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

public interface IResource {

	public File getProjectBase();

	public String getSourcePath();
	
	public File getSourceFile();

	public String getTargetPath();
	
	public File getTargetFile();

}
