
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.RhenaResolver;

/**
 * Should in deal with InMemory models (not implemented?)
 * 
 * @author noname
 *
 */
@Ignore
public class TestCascadingResolverPerformance extends AbstractRhenaConfiguredTest {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testPerformance() throws Exception {

		IRhenaConfiguration config = createMockConfig();
		RhenaResolver resolver = new RhenaResolver();
		IRhenaContext context = createMockContext(config, resolver);

		long iterations = 1000000;
		long totalTime = 0;
		for (int i = 0; i < iterations; i++) {
			long start = System.currentTimeMillis();
			CascadingModelResolver cascadingResolver = new CascadingModelResolver(context);
			IRhenaModule module = cascadingResolver.resolveModuleTree(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
			Assert.assertNotNull(module);
			totalTime += (System.currentTimeMillis() - start);
		}

		long perExec = (totalTime / iterations);
		log.info("Execution of " + iterations + " iterations of " + CascadingModelResolver.class.getName() + " resulted in: " + perExec + "ms/exec");
	}
}
