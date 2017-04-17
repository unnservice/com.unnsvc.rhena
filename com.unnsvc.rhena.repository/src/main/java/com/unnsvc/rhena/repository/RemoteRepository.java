
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public class RemoteRepository extends AbstractRepository {

	public RemoteRepository(IRepositoryDefinition definition) {

		super(definition);
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier moduleIdentifier) {

		// @TODO must also set the module type after parsing
		throw new UnsupportedOperationException("Not implemented");
	}

}
