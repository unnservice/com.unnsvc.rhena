
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRepository;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.core.config.RhenaConfiguration;

public class RhenaResolver implements IRhenaResolver {

	private IRepositoryConfiguration repositoryConfiguration;

	public RhenaResolver(RhenaConfiguration config) {

		this.repositoryConfiguration = config.getRepositoryConfiguration();
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		// first try workspace resolvers
		try {
			for (IRepository repository : repositoryConfiguration.getWorkspaceRepositories()) {
				
				return repository.resolveModule(identifier);
			}
		} catch (NotFoundException nfe) {
			
			// @TODO perform trace logging
		}

		// after try cache
		try {
			
			repositoryConfiguration.getCacheRepository().resolveModule(identifier);
		} catch (NotFoundException nfe) {
			// @TODO perform trace logging
		}

		// after try remote resolvers
		
		try {
			
			for(IRepository repository : repositoryConfiguration.getRemoteRepositories()) {
				
				return repository.resolveModule(identifier);
			}
		} catch (NotFoundException nfe) {
			
			// @TODO perform debug logging
		}
		
		throw new NotFoundException("Failed to resolve " + identifier);
	}

}
