
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

public class RemoteRepository extends AbstractRepository {

	public RemoteRepository(RepositoryIdentifier identifier, IRhenaCache cache) {

		super(identifier, cache);
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) {

		throw new UnsupportedOperationException("Not implemented");
	}

}
