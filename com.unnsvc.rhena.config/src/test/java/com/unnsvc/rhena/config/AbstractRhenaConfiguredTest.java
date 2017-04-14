
package com.unnsvc.rhena.config;

import java.io.File;

import org.junit.Before;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;

public class AbstractRhenaConfiguredTest extends AbstractRhenaTest {

	private IRhenaConfiguration config;

	@Before
	public void before() throws Exception {

		this.config = new RhenaConfiguration();

		File thisProject = new File("").getAbsoluteFile().getCanonicalFile();
		File repositoriesLocation = new File(thisProject, "src/test/resources/repositories");
		File workspaceRepo = new File(repositoriesLocation, "workspaceRepo");
		File localRepo = new File(repositoriesLocation, "localRepo");

		this.config.getRepositoryConfiguration().setCacheRepository(RepositoryDefinition.newLocal(localRepo.getName(), localRepo.toURI()));
		this.config.getRepositoryConfiguration().addWorkspaceRepositories(RepositoryDefinition.newWorkspace(workspaceRepo.getName(), workspaceRepo.toURI()));
	}

	public IRhenaConfiguration getConfig() {

		return config;
	}
}
