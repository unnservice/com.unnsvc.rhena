
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

public class TestCustomLifecycle extends AbstractRhenaTest {

	@Test
	public void testModule() throws Exception {

		getContext().addWorkspaceRepository(new WorkspaceRepository(getContext(), new File("/home/noname/workspaces/runtime-New_configuration")));

		IRhenaEngine engine = new RhenaEngine(getContext());

		IRhenaModule entryPointModule = engine.materialiseModel(ModuleIdentifier.valueOf("com.test:module1:0.0.1"));
		Assert.assertNotNull(entryPointModule);

		debugContext(engine);

		IRhenaExecution execution = engine.materialiseExecution(new CommandCaller(entryPointModule, EExecutionType.TEST, "testCommand"));
		Assert.assertNotNull(execution);

		// because we want to configure eclipse with it
		Assert.assertTrue(execution instanceof WorkspaceExecution);
	}
}
