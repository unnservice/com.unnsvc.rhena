
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;
import java.io.Serializable;

public interface IResource extends Serializable {

	public EResourceType getResourceType();

	public String getRelativePath();

	public File getAbsolutePath();

}
