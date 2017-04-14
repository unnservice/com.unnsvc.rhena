
package com.unnsvc.rhena.repository;

import java.io.File;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

public class CacheRepository extends LocalRepository {

	public CacheRepository(RepositoryIdentifier identifier, File location, IRhenaCache cache) {

		super(identifier, location, cache);
	}

}
