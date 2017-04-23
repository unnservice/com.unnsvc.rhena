
package com.unnsvc.rhena.repository;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepository;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.RepositoryDefinition;

public class RhenaResolver implements IRhenaResolver {

	private Logger log = LoggerFactory.getLogger(getClass());

	public RhenaResolver() {

	}

	@Override
	public IRhenaModule resolveModule(IRhenaContext context, ModuleIdentifier identifier) throws RhenaException {

		IRepositoryConfiguration repoConfig = context.getConfig().getRepositoryConfiguration();

		IRhenaModule resolved = null;

		if (resolved == null) {

			resolved = recursiveWorkspaceSearch(context, identifier);
			if (resolved != null) {
				return resolved;
			}
		}

		if (resolved == null && repoConfig.getCacheRepository() != null) {
			resolved = resolve(context, identifier, repoConfig.getCacheRepository());
			if (resolved != null) {
				return resolved;
			}
		}

		if (resolved == null) {
			for (IRepositoryDefinition definition : repoConfig.getRemoteRepositories()) {

				resolved = resolve(context, identifier, definition);
				if (resolved != null) {
					return resolved;
				}
			}
		}

		throw new NotFoundException("Failed to resolve " + identifier);
	}

	protected IRhenaModule resolve(IRhenaContext context, ModuleIdentifier identifier, IRepositoryDefinition definition) throws RhenaException {

		IRepository repository = context.getFactories().getRepositoryFactory().createRepository(context, definition);

		try {
			IRhenaModule module = repository.resolveModule(identifier);
			log.info(identifier + " resolved from: " + repository.getDefinition());
			return module;
		} catch (NotFoundException nfe) {
			log.debug(identifier + " not found in: " + repository.getDefinition());
			return null;
		}
	}

	protected IRhenaModule recursiveWorkspaceSearch(IRhenaContext context, ModuleIdentifier identifier) throws RhenaException {

		IRepositoryConfiguration repoConfig = context.getConfig().getRepositoryConfiguration();
		Set<File> repositoryLocation = new HashSet<File>();
		repoConfig.getWorkspaceRepositories().forEach(defn -> repositoryLocation.add(new File(defn.getLocation().getPath())));

		while (!repositoryLocation.isEmpty()) {

			for (Iterator<File> iter = repositoryLocation.iterator(); iter.hasNext();) {

				File location = iter.next();
				iter.remove();

				RepositoryIdentifier repoId = new RepositoryIdentifier(location.getName());
				IRepositoryDefinition defn = new RepositoryDefinition(ERepositoryType.WORKSPACE, repoId, location.getAbsoluteFile().toURI());

				// try this location first, before trying sublocations
				IRhenaModule resolved = resolve(context, identifier, defn);
				if (resolved != null) {
					return resolved;
				} else {

					boolean added = false;
					// add subdirectories as repositories, if the subdirectories
					// are modules
					for (File innerLocation : location.listFiles()) {
						File moduleFile = new File(innerLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
						if (moduleFile.isFile()) {
							added = repositoryLocation.add(innerLocation);
						}
					}

					if (added) {
						// break for loop with iterator so we first process
						// inner
						// directories
						break;
					}
				}
			}
		}

		return null;
	}

}
