
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.repository.IRepository;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public abstract class AbstractRepository implements IRepository {

	private IRepositoryDefinition definition;
	private IRhenaCache cache;

	public AbstractRepository(IRepositoryDefinition definition, IRhenaCache cache) {

		this.definition = definition;
		this.cache = cache;
	}

	@Override
	public IRepositoryDefinition getDefinition() {

		return definition;
	}

	@Override
	public void setDefinition(IRepositoryDefinition definition) {

		this.definition = definition;
	}

	public void setCache(IRhenaCache cache) {

		this.cache = cache;
	}

	public IRhenaCache getCache() {

		return cache;
	}
}
