
package com.unnsvc.rhena.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.MockCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IBuilderFactory;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.IRepositoryFactory;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

public abstract class AbstractRhenaConfiguredTest extends AbstractRhenaTest {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected IRhenaConfiguration createMockConfig() throws RhenaException {

		try {
			log.debug("Creating mock IRhenaConfiguration");
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

			config.setAgentAddress(ObjectServerHelper.availableAddress());

			return config;
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	protected IRhenaCache createMockCache() {

		return new MockCache();
	}

	protected IRhenaContext createMockContext() throws RhenaException {

		return createMockContext(createMockConfig(), createMockCache(), new IRhenaResolver() {

			@Override
			public IRhenaModule resolveModule(IRhenaContext context, ModuleIdentifier identifier) throws RhenaException {

				throw new UnsupportedOperationException("Not implemented for testing");
			}
		}, createMockFactories());
	}

	protected IRhenaContext createMockContext(IRhenaConfiguration config, IRhenaResolver resolver) throws RhenaException {

		return createMockContext(createMockConfig(), createMockCache(), resolver, createMockFactories());
	}

	public IRhenaContext createMockContext(IRhenaConfiguration config, IRhenaResolver resolver, IRhenaFactories factories) throws RhenaException {

		return createMockContext(config, createMockCache(), resolver, factories);
	}

	/**
	 * @TODO provide these as an abstract method instead of this ugly empty
	 *       implementation
	 * @return
	 */
	protected IRhenaFactories createMockFactories() {

		IRhenaFactories factories = new IRhenaFactories() {

			@Override
			public IRhenaAgentClientFactory getAgentClientFactory() {

				throw new UnsupportedOperationException("Not implemented for testing?");
			}

			@Override
			public IRepositoryFactory getRepositoryFactory() {

				throw new UnsupportedOperationException("Not implemented for testing?");
			}

			@Override
			public IBuilderFactory getBuilderFactory() {

				throw new UnsupportedOperationException("Not implemented for testing?");
			}

		};
		return factories;
	}

	protected IRhenaContext createMockContext(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver, IRhenaFactories factories) {

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

			@Override
			public IRhenaFactories getFactories() {

				return factories;
			}
		};
	}
}
