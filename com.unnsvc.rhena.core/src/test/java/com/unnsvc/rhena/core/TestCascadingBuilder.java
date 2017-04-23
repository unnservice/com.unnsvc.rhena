
package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.resolution.CascadingModelBuilder;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.RhenaResolver;

public class TestCascadingBuilder extends AbstractAgentTest {

	@Test
	public void testBuilder() throws Exception {

		IRhenaConfiguration config = createMockConfig();
		RhenaResolver resolver = new RhenaResolver();
		IRhenaContext context = createMockContext(config, resolver);

		CascadingModelResolver modelResolver = new CascadingModelResolver(context);

		EExecutionType type = EExecutionType.TEST;
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.multi:module1:1.0.0");

		IRhenaModule module = modelResolver.resolveModuleTree(identifier);

		CascadingModelBuilder modelBuilder = new CascadingModelBuilder(context);
		modelBuilder.executeModel(type, identifier);
	}

}
