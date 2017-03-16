
package com.unnsvc.rhena.common.config;

import java.io.File;
import java.io.Serializable;

public interface IRhenaConfiguration extends Serializable {

	public void setInstanceHome(File instanceHome);

	public File getInstanceHome();

	public IRepositoryConfiguration getRepositoryConfiguration();

	public IBuildConfiguration getBuildConfiguration();

}
