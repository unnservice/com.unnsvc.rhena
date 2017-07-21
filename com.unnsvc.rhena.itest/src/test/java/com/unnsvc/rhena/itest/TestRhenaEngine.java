
package com.unnsvc.rhena.itest;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaEngine;

public class TestRhenaEngine extends AbstractIntegrationTest {

	@Test
	public void testEngine() throws Exception {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:complex:1.0.0");

		IRhenaEngine engine = new RhenaEngine(getConfig());

		IRhenaModule module = engine.resolveModule(identifier);
		IExecutionResponse result = engine.resolveExecution(EExecutionType.ITEST, module);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof IExecutionResponse);
	}
}
