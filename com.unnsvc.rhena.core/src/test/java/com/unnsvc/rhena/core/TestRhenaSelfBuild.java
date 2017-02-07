
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

public class TestRhenaSelfBuild extends AbstractRhenaTest {

	@Test
	public void testModule() throws Exception {
		
		getContext().addWorkspaceRepository(new WorkspaceRepository(getContext(), new File("../../")));
		getContext().addWorkspaceRepository(new WorkspaceRepository(getContext(), new File("../")));

		IRhenaEngine engine = new RhenaEngine(getContext());

		IRhenaModule entryPointModule = engine.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:core:1.0.0"));
		Assert.assertNotNull(entryPointModule);

		debugContext(engine);

		IRhenaExecution execution = engine.materialiseExecution(new Caller(entryPointModule, EExecutionType.TEST));
		Assert.assertNotNull(execution);

		// because we want to configure eclipse with it
		Assert.assertTrue(execution instanceof WorkspaceExecution);
	}
}
