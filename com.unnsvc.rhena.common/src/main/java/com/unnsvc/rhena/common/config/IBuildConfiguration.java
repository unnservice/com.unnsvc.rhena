
package com.unnsvc.rhena.common.config;

public interface IBuildConfiguration {

	public void setFullBuild(boolean fullBuild);

	/**
	 * Full build implies that the highest execution of the project is produced
	 * instead of lowest necessary (for example MAIN because it is required by
	 * model instead of ITEST)
	 * 
	 * @return
	 */
	public boolean isFullBuild();

}
