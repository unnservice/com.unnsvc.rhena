
package com.unnsvc.rhena.repository;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.config.RepositoryDefinition;

public class TestWorkspaceRepository extends AbstractRhenaConfiguredTest {

	@Test
	public void testRepository() throws Exception {

		File location = new File(new File("").getCanonicalFile(), "../test-repositories/workspaceRepo/");

		WorkspaceRepository local = new WorkspaceRepository(RepositoryDefinition.newLocal("test", location.toURI()));
		IRhenaModule module1 = local.resolveModule(ModuleIdentifier.valueOf("com.test:simple:1.0.0"));
		Assert.assertNotNull(module1);
	}
}
