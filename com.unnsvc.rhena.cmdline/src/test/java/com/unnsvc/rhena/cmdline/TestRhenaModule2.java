
package com.unnsvc.rhena.cmdline;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

public class TestRhenaModule2 {

	@Test
	public void testModule() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.addRepository(new WorkspaceRepository(new File("../../")));
		config.addRepository(new WorkspaceRepository(new File("../")));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(true);
		// Produce packages or use exploded compilation
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);
		// config.setLogHandler(IRhenaLogHandler logHandler);
		// config.getRepositoryConfiguration().addRepository()
		// config.getRepositoryConfiguration().setProxyXX?
		// config.getTestConfiguration().setXXX
		// config.addListener...

		/**
		 * This portion below can be executed multiple times, make sure there
		 * are no resource leaks in the lifecycles
		 */
		try {
			
			IRhenaContext context = new RhenaContext(config);
			
			IRhenaModule entryPointModule = context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:core:0.0.1"));
			Assert.assertNotNull(entryPointModule);
			
			IRhenaExecution execution = context.materialiseExecution(entryPointModule, EExecutionType.PROTOTYPE);
			Assert.assertNotNull(execution);

			// because we want to configure eclipse with it
			Assert.assertTrue(execution instanceof WorkspaceExecution);

			// introspect model?
		} catch (RhenaException ex) {
			
			throw new Exception(ex);
		}
	}
}
