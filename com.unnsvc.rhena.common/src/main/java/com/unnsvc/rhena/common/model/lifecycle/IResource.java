
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

public interface IResource {

	public File getSource();

	public File getTarget();

	public boolean isStaged();

}
