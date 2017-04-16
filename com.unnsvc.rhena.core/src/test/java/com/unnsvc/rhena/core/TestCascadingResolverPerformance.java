
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.resolver.RhenaResolver;

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

		RhenaResolver rhenaResolver = new RhenaResolver(getConfig());

		long iterations = 1000000;
		long totalTime = 0;
		for (int i = 0; i < iterations; i++) {
			long start = System.currentTimeMillis();
			CascadingModelResolver resolver = new CascadingModelResolver(getConfig(), rhenaResolver, getMockCache());
			IRhenaModule module = resolver.resolveModuleTree(ModuleIdentifier.valueOf("com.multi:module1:1.0.0"));
			Assert.assertNotNull(module);
			totalTime += (System.currentTimeMillis() - start);
		}

		long perExec = (totalTime / iterations);
		log.info("Execution of " + iterations + " iterations of " + CascadingModelResolver.class.getName() + " resulted in: " + perExec + "ms/exec");
	}
}
