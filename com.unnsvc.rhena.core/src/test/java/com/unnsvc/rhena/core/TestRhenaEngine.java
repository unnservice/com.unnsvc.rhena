
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class TestRhenaEngine extends AbstractAgentTest {

	@Test
	public void testEngine() throws Exception {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:complex:1.0.0");
		IRhenaConfiguration config = createMockConfig();
		IRhenaEngine engine = new RhenaEngine(config);
		
		IRhenaModule module = engine.resolveModule(identifier);
		IExecutionResult result = engine.resolveExecution(EExecutionType.ITEST, module);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof IExecutionResult);
	}
}
