
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.ng.repository.IRepository;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public abstract class AbstractRepository implements IRepository {

	private IRepositoryDefinition definition;

	public AbstractRepository(IRepositoryDefinition definition) {

		this.definition = definition;
	}

	@Override
	public IRepositoryDefinition getDefinition() {

		return definition;
	}

	@Override
	public void setDefinition(IRepositoryDefinition definition) {

		this.definition = definition;
	}
}
