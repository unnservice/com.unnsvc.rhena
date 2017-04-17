
package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelBuilder;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.resolver.RhenaResolver;

public class TestCascadingBuilder extends AbstractRhenaConfiguredTest {

	@Test
	public void testBuilder() throws Exception {

		RhenaResolver resolver = new RhenaResolver(getConfig());
		EExecutionType type = EExecutionType.MAIN;
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.multi:module1:1.0.0");

		CascadingModelResolver modelResolver = new CascadingModelResolver(resolver, getMockCache());
		IRhenaModule module = modelResolver.resolveModuleTree(identifier);

		CascadingModelBuilder modelBuilder = new CascadingModelBuilder(getConfig(), getMockCache(), resolver);
		modelBuilder.executeModel(type, identifier);
	}
}
