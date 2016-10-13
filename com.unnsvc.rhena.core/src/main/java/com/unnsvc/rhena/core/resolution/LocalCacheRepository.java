
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

/**
 * @TODO Should also allow for installation..
 * 
 * @author noname
 *
 */
public class LocalCacheRepository extends RemoteRepository {

	public LocalCacheRepository(URI location) {

		super(location);
	}

}
