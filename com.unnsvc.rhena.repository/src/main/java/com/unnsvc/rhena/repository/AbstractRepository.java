
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.repository.IRepository;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

public abstract class AbstractRepository implements IRepository {

	private RepositoryIdentifier identifier;
	private IRhenaCache cache;

	public AbstractRepository(RepositoryIdentifier identifier, IRhenaCache cache) {

		this.identifier = identifier;
		this.cache = cache;
	}

	@Override
	public void setIdentifier(RepositoryIdentifier identifier) {

		this.identifier = identifier;
	}

	@Override
	public RepositoryIdentifier getIdentifier() {

		return identifier;
	}

	public void setCache(IRhenaCache cache) {

		this.cache = cache;
	}

	public IRhenaCache getCache() {

		return cache;
	}
}
