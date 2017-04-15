
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public class RemoteRepository extends AbstractRepository {

	public RemoteRepository(IRepositoryDefinition definition, IRhenaCache cache) {

		super(definition, cache);
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) {

		throw new UnsupportedOperationException("Not implemented");
	}

}
