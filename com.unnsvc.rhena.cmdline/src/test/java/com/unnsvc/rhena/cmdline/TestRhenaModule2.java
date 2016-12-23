
package com.unnsvc.rhena.cmdline;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.SystemOutLogFactory;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.DebugModelVisitor;

public class TestRhenaModule2 {

	@Test
	public void testModule() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.addRepository(new WorkspaceRepository(config, new File("../../")));
		config.addRepository(new WorkspaceRepository(config, new File("../")));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(false);
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

			IRhenaEngine engine = new RhenaEngine(config);

			IRhenaModule entryPointModule = engine.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:core:0.0.1"));
			Assert.assertNotNull(entryPointModule);

			debugContext(engine);

			IRhenaExecution execution = engine.materialiseExecution(entryPointModule, EExecutionType.PROTOTYPE);
			Assert.assertNotNull(execution);

			// because we want to configure eclipse with it
			Assert.assertTrue(execution instanceof WorkspaceExecution);

			// introspect model?
		} catch (RhenaException ex) {

			throw new Exception(ex);
		}
	}

	private void debugContext(IRhenaEngine engine) throws RhenaException {

		engine.getCache().getModules().forEach((identifier, module) -> {
			try {
				module.visit(new DebugModelVisitor(engine.getConfiguration(), 0, engine));
			} catch (RhenaException e) {

				e.printStackTrace();
			}
		});
	}
}
