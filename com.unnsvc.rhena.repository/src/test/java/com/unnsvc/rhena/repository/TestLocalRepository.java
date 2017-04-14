
package com.unnsvc.rhena.repository;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;

public class TestLocalRepository extends AbstractRhenaConfiguredTest {

	@Test
	public void testRepository() throws Exception {

		File repositoryLocation = new File(new File("").getCanonicalFile(), "src/test/resources/repository");
		RepositoryIdentifier identifier = new RepositoryIdentifier("test");

		LocalRepository local = new LocalRepository(identifier, repositoryLocation, getMockCache());
		IRhenaModule module1 = local.resolveModule(ModuleIdentifier.valueOf("com.test:module1:1.0.0"));
		Assert.assertNotNull(module1);
	}
}