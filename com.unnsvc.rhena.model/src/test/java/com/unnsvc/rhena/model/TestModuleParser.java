
package com.unnsvc.rhena.model;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.model.parser.RhenaModuleParser;

public class TestModuleParser extends AbstractRhenaTest {

	@Test
	public void testParseModule() throws Exception {

		File repositoryLocation = new File(new File("").getCanonicalFile(), "src/test/resources/repository");
		File moduleLocation = new File(repositoryLocation, "com.test.module");
		URI moduleDescriptorLocation = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME).toURI();
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:module:1.0.0");

		RhenaModuleParser parser = new RhenaModuleParser(new RepositoryIdentifier("test"), identifier, moduleDescriptorLocation);
		IRhenaModule module = parser.getModule();
		Assert.assertNotNull(module);

		ILifecycleSpecification lifecycleSpec = module.getLifecycleSpecification();
		Assert.assertNotNull(lifecycleSpec);
		
		List<IRhenaEdge> lifecycleDeps = lifecycleSpec.getLifecycleDependencies();
		Assert.assertNotNull(lifecycleDeps);
		Assert.assertEquals(1, lifecycleDeps.size());
	}
}
