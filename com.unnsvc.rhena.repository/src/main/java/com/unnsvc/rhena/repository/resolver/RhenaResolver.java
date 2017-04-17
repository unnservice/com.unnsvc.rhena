
package com.unnsvc.rhena.repository.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepository;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.repository.LocalRepository;
import com.unnsvc.rhena.repository.RemoteRepository;
import com.unnsvc.rhena.repository.WorkspaceRepository;

public class RhenaResolver implements IRhenaResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepositoryConfiguration repositoryConfiguration;

	public RhenaResolver(IRhenaConfiguration config) {

		this.repositoryConfiguration = config.getRepositoryConfiguration();
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

			return new LocalRepository(definition);
		} else if (definition.getRepositoryType().equals(ERepositoryType.WORKSPACE)) {

			return new WorkspaceRepository(definition);
		} else if (definition.getRepositoryType().equals(ERepositoryType.REMOTE)) {

			return new RemoteRepository(definition);
		}

		throw new RhenaException("Unknown repository type: " + definition.getRepositoryType());
	}

}
