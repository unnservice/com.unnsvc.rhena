
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;

public class TestCascadingResolver extends AbstractRhenaConfiguredTest {

	@Test
	public void testEngine() throws RhenaException {

		CascadingModelResolver resolver = new CascadingModelResolver(getConfig(), getMockCache());
		IRhenaModule module = resolver.resolveModule(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
		Assert.assertNotNull(module);
		
		// @TODO verify what's in the cache?
	}
}
