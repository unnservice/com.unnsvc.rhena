
package com.unnsvc.rhena.repository;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepository;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.IRepositoryFactory;

public class RepositoryFactory implements IRepositoryFactory {

	@Override
	public IRepository createRepository(IRhenaContext context, IRepositoryDefinition definition) throws RhenaException {

		if (definition.getRepositoryType().equals(ERepositoryType.CACHE)) {

			return new LocalRepository(definition);
		} else if (definition.getRepositoryType().equals(ERepositoryType.WORKSPACE)) {

			return new WorkspaceRepository(definition);
		} else if (definition.getRepositoryType().equals(ERepositoryType.REMOTE)) {

			return new RemoteRepository(definition);
		}

		throw new RhenaException("Unknown repository type: " + definition.getRepositoryType());
	}

}
