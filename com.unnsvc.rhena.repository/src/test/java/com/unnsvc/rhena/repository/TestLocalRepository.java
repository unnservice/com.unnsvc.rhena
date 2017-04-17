
package com.unnsvc.rhena.repository;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.config.RepositoryDefinition;

public class TestLocalRepository extends AbstractRhenaConfiguredTest {

	@Test
	public void testRepository() throws Exception {

		File location = new File(new File("").getCanonicalFile(), "../test-repositories/localRepo/");

		LocalRepository local = new LocalRepository(RepositoryDefinition.newLocal("test", location.toURI()));
		IRhenaModule module1 = local.resolveModule(ModuleIdentifier.valueOf("com.single:module1:1.0.0"));
		Assert.assertNotNull(module1);
	}
}
