
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

public interface IResource {

	public EResourceType getResourceType();

	public String getRelativePath();

	public File getAbsolutePath();

}
