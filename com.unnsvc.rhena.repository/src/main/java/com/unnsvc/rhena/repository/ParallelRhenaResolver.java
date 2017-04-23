
package com.unnsvc.rhena.repository;

/**
 * The parallel resolver is meant to preload all of the workspace modules, this
 * to later provide a means for detecting overarching entry points. It will load
 * all modules from all workspaces in parallel so they will be available in the
 * provided cache by the time we start resolutions.
 * 
 * @author noname
 *
 */
public class ParallelRhenaResolver extends RhenaResolver {

	public ParallelRhenaResolver() {

	}

}
