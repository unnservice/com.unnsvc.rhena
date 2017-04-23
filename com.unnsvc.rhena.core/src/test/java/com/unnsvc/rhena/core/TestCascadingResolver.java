
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.RhenaResolver;

public class TestCascadingResolver extends AbstractRhenaConfiguredTest {

	@Test
	public void testEngine() throws Exception {

		IRhenaConfiguration config = createMockConfig();
		IRhenaResolver resolver = new RhenaResolver();
		IRhenaContext context = createMockContext(config, resolver);

		CascadingModelResolver cascadingResolver = new CascadingModelResolver(context);
		IRhenaModule module = cascadingResolver.resolveModuleTree(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
		Assert.assertNotNull(module);

		// @TODO verify what's in the cache?
	}
}
