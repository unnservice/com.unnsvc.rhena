
package com.unnsvc.rhena.repository.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
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

		IRhenaModule resolved = null;

		if (resolved == null) {
			for (IRepositoryDefinition definition : repositoryConfiguration.getWorkspaceRepositories()) {
				resolved = resolve(identifier, definition);
				if (resolved != null) {
					return resolved;
				}
			}
		}

		if (resolved == null && repositoryConfiguration.getCacheRepository() != null) {
			resolved = resolve(identifier, repositoryConfiguration.getCacheRepository());
			if (resolved != null) {
				return resolved;
			}
		}

		if (resolved == null) {
			for (IRepositoryDefinition definition : repositoryConfiguration.getRemoteRepositories()) {

				resolved = resolve(identifier, definition);
				if (resolved != null) {
					return resolved;
				}
			}
		}

		throw new NotFoundException("Failed to resolve " + identifier);
	}

	private IRhenaModule resolve(ModuleIdentifier identifier, IRepositoryDefinition definition) throws RhenaException {

		IRepository repository = createRepository(definition);

		try {
			IRhenaModule module = repository.resolveModule(identifier);
			log.info("Resolved " + identifier + " from " + repository);
			return module;
		} catch (NotFoundException nfe) {
			log.debug(identifier + " not found in " + repository);
			return null;
		}
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
