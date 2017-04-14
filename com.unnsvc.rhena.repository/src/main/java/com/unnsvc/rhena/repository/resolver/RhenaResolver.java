
package com.unnsvc.rhena.repository.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.ERepositoryType;
import com.unnsvc.rhena.common.ng.repository.IRepository;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.repository.LocalRepository;
import com.unnsvc.rhena.repository.RemoteRepository;
import com.unnsvc.rhena.repository.WorkspaceRepository;

public class RhenaResolver implements IRhenaResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepositoryConfiguration repositoryConfiguration;
	private IRhenaCache cache;

	public RhenaResolver(IRhenaConfiguration config, IRhenaCache cache) {

		this.repositoryConfiguration = config.getRepositoryConfiguration();
		this.cache = cache;
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		// first try workspace resolvers
		try {
			for (IRepositoryDefinition definition : repositoryConfiguration.getWorkspaceRepositories()) {

				log.debug("Try resolve " + identifier + " in " + definition);
				IRepository repository = createRepository(definition);
				return repository.resolveModule(identifier);
			}
		} catch (NotFoundException nfe) {

			log.debug(identifier + " not found in WorkspaceRepositories");
		}

		try {

			IRepositoryDefinition definition = repositoryConfiguration.getCacheRepository();
			if (definition != null) {
				
				log.debug("Try resolve " + identifier + " in " + definition);
				IRepository repository = createRepository(definition);
				return repository.resolveModule(identifier);
			}
		} catch (NotFoundException nfe) {

			log.debug(identifier + " not found in CacheRepository");
		}

		// after try remote resolvers

		try {

			for (IRepositoryDefinition definition : repositoryConfiguration.getRemoteRepositories()) {
				
				log.debug("Try resolve " + identifier + " in " + definition);
				IRepository repository = createRepository(definition);
				return repository.resolveModule(identifier);
			}
		} catch (NotFoundException nfe) {

			log.debug(identifier + " not found in RemoteRepositories");
		}

		throw new NotFoundException("Failed to resolve " + identifier);
	}

	public IRepository createRepository(IRepositoryDefinition definition) throws RhenaException {

		if (definition.getRepositoryType().equals(ERepositoryType.LOCAL)) {

			return new LocalRepository(definition, cache);
		} else if (definition.getRepositoryType().equals(ERepositoryType.WORKSPACE)) {

			return new WorkspaceRepository(definition, cache);
		} else if (definition.getRepositoryType().equals(ERepositoryType.REMOTE)) {

			return new RemoteRepository(definition, cache);
		}

		throw new RhenaException("Unknown repository type: " + definition.getRepositoryType());
	}

}
