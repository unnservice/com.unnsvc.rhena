
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.AbstractRhenaTest;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.RhenaConfiguration;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;

public class TestModelResolver extends AbstractRhenaTest {

	@Test
	public void testResolver() throws Exception {

		RhenaConfiguration config = new RhenaConfiguration();
		RhenaResolver resolver = new RhenaResolver(config);
		
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:module1:1.0.0");
		CascadingModelResolver casccadingResolver = new CascadingModelResolver(config, getMockCache());
		IRhenaModule module = casccadingResolver.resolveModule(identifier);
		Assert.assertNotNull(module);
		// @TODO verify cache
	}
}
