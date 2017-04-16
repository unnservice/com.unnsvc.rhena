
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.resolver.RhenaResolver;

public class TestCascadingResolver extends AbstractRhenaConfiguredTest {

	@Test
	public void testEngine() throws RhenaException {

		RhenaResolver rhenaResolver = new RhenaResolver(getConfig(), getMockCache());

		CascadingModelResolver resolver = new CascadingModelResolver(getConfig(), rhenaResolver, getMockCache());
		IRhenaModule module = resolver.resolveModuleTree(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
		Assert.assertNotNull(module);
		
		// @TODO verify what's in the cache?
	}
}
