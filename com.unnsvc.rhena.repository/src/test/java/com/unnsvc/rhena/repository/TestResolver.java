
package com.unnsvc.rhena.repository;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;

public class TestResolver extends AbstractRhenaConfiguredTest {

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
