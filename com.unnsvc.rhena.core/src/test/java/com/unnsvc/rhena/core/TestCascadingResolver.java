
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.RhenaResolver;

public class TestCascadingResolver extends AbstractRhenaConfiguredTest {

	@Test
	public void testEngine() throws RhenaException {

		RhenaResolver rhenaResolver = new RhenaResolver(getConfig());

		CascadingModelResolver resolver = new CascadingModelResolver(rhenaResolver, getMockCache());
		IRhenaModule module = resolver.resolveModuleTree(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
		Assert.assertNotNull(module);
		
		// @TODO verify what's in the cache?
	}
}
