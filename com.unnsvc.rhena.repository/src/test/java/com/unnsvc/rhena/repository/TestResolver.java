
package com.unnsvc.rhena.repository;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.repository.resolver.RhenaResolver;

public class TestResolver extends AbstractRhenaConfiguredTest {

	@Test
	public void testResolver() throws Exception {

		RhenaResolver resolver = new RhenaResolver(getConfig());

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.single:module1:1.0.0");
		IRhenaModule module = resolver.resolveModule(identifier);
		Assert.assertNotNull(module);
		// @TODO verify cache
	}
}
