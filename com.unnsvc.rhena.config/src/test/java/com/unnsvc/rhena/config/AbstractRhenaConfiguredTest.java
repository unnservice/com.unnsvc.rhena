
package com.unnsvc.rhena.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.MockCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.IRhenaResolver;

public abstract class AbstractRhenaConfiguredTest extends AbstractRhenaTest {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected IRhenaConfiguration createMockConfig() throws Exception {

		log.debug("Creating IRhenaConfiguration");
		IRhenaConfiguration config = new RhenaConfiguration();

		File testRepositoriesLocation = new File("../test-repositories").getAbsoluteFile().getCanonicalFile();

		File localRepo = new File(testRepositoriesLocation, "localRepo");
		File workspaceRepo = new File(testRepositoriesLocation, "workspaceRepo");

		IRepositoryDefinition localRepoDef = RepositoryDefinition.newLocal(localRepo.getName(), localRepo.toURI());
		log.debug(localRepoDef.toString());
		config.getRepositoryConfiguration().setCacheRepository(localRepoDef);

		IRepositoryDefinition workspaceRepoDef = RepositoryDefinition.newWorkspace(workspaceRepo.getName(), workspaceRepo.toURI());
		log.debug(workspaceRepoDef.toString());
		config.getRepositoryConfiguration().addWorkspaceRepositories(workspaceRepoDef);

		/**
		 * During testing, we'd want predictable sequential execution unless
		 * specified otherwise
		 */
		config.setThreads(1);
		return config;
	}

	protected IRhenaCache createMockCache() {

		return new MockCache();
	}

	protected IRhenaContext createMockContext() throws Exception {

		return createMockContext(createMockConfig(), createMockCache(), new IRhenaResolver() {

			@Override
			public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

				throw new UnsupportedOperationException("Not implemented for testing");
			}
		});
	}

	protected IRhenaContext createMockContext(IRhenaConfiguration config, IRhenaResolver resolver) throws Exception {

		return createMockContext(createMockConfig(), createMockCache(), resolver);
	}

	protected IRhenaContext createMockContext(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		return new IRhenaContext() {

			@Override
			public IRhenaResolver getResolver() {

				return resolver;
			}

			@Override
			public IRhenaConfiguration getConfig() {

				return config;
			}

			@Override
			public IRhenaCache getCache() {

				return cache;
			}
		};
	}
}
