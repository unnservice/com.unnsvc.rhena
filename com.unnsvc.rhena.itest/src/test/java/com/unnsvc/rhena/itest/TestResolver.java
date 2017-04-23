package com.unnsvc.rhena.itest;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.repository.RhenaResolver;

public class TestResolver extends AbstractIntegrationTest {

	@Test
	public void testResolver() throws Exception {

		IRhenaConfiguration config = createMockConfig();
		RhenaResolver resolver = new RhenaResolver();

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.single:module1:1.0.0");
		IRhenaModule module = resolver.resolveModule(createMockContext(), identifier);
		Assert.assertNotNull(module);
		// @TODO verify cache
	}
}