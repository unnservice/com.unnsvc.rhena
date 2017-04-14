
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
		log.info("Creating IRhenaConfiguration");
		this.config = new RhenaConfiguration();

		File thisProject = new File("").getAbsoluteFile().getCanonicalFile();
		File repositoriesLocation = new File(thisProject, "src/test/resources/repositories");
		File workspaceRepo = new File(repositoriesLocation, "workspaceRepo");
		File localRepo = new File(repositoriesLocation, "localRepo");

		IRepositoryDefinition localRepoDef = RepositoryDefinition.newLocal(localRepo.getName(), localRepo.toURI());
		log.info(localRepoDef.toString());
		this.config.getRepositoryConfiguration().setCacheRepository(localRepoDef);

		IRepositoryDefinition workspaceRepoDef = RepositoryDefinition.newWorkspace(workspaceRepo.getName(), workspaceRepo.toURI());
		log.info(workspaceRepoDef.toString());
		this.config.getRepositoryConfiguration().addWorkspaceRepositories(workspaceRepoDef);
	}

	public IRhenaConfiguration getConfig() {

		return config;
	}
}
