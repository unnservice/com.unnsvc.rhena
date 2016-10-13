
package com.unnsvc.rhena.core.configuration;

import java.io.File;
import java.net.URI;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaConfiguration {

	private URI localCacheRepository;

	public RhenaConfiguration() {

		File userHome = new File(System.getProperty("user.home"));
		File rhenaHome = new File(userHome, ".rhena");
		localCacheRepository = new File(rhenaHome, "repository").getAbsoluteFile().toURI();
	}

	public URI getLocalCacheRepository() {

		return localCacheRepository;
	}
}
