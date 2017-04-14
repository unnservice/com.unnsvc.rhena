
package com.unnsvc.rhena.config;

import java.io.File;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.repository.IRepositoryDefinition;

public abstract class AbstractRhenaConfiguredTest extends AbstractRhenaTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaConfiguration config;

	@Before
	public void before() throws Exception {

		super.before();
		log.debug("Creating IRhenaConfiguration");
		this.config = new RhenaConfiguration();

		File testRepositoriesLocation = new File("../test-repositories").getAbsoluteFile().getCanonicalFile();
		
		File localRepo = new File(testRepositoriesLocation, "localRepo");
		File workspaceRepo = new File(testRepositoriesLocation, "workspaceRepo");

		IRepositoryDefinition localRepoDef = RepositoryDefinition.newLocal(localRepo.getName(), localRepo.toURI());
		log.debug(localRepoDef.toString());
		this.config.getRepositoryConfiguration().setCacheRepository(localRepoDef);

		IRepositoryDefinition workspaceRepoDef = RepositoryDefinition.newWorkspace(workspaceRepo.getName(), workspaceRepo.toURI());
		log.debug(workspaceRepoDef.toString());
		this.config.getRepositoryConfiguration().addWorkspaceRepositories(workspaceRepoDef);
	}

	public IRhenaConfiguration getConfig() {

		return config;
	}
}
