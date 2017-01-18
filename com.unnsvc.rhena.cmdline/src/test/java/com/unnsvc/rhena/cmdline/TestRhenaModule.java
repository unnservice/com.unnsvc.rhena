
package com.unnsvc.rhena.cmdline;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.Caller;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.logging.SystemOutLogListener;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.DebugModelVisitor;

public class TestRhenaModule {

	@Test
	public void testModule() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.setParallel(false);
		// Produce packages or use exploded compilation
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);
		// context.setLogHandler(IRhenaLogHandler logHandler);
		// context.getRepositoryConfiguration().addRepository()
		// context.getRepositoryConfiguration().setProxyXX?
		// context.getTestConfiguration().setXXX
		// context.addListener...

		/**
		 * This portion below can be executed multiple times, make sure there
		 * are no resource leaks in the lifecycles
		 */
		try (IRhenaContext context = new RhenaContext(config)) {
			context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../../")));
			context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../")));
			context.setLocalRepository(new LocalCacheRepository(context));
			context.getListenerConfig().addListener(new SystemOutLogListener());

			IRhenaEngine engine = new RhenaEngine(context);

			IRhenaModule entryPointModule = engine.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:core:0.0.1"));
			Assert.assertNotNull(entryPointModule);

			debugContext(engine);

			IRhenaExecution execution = engine.materialiseExecution(new Caller(entryPointModule, EExecutionType.TEST));
			Assert.assertNotNull(execution);

			// because we want to configure eclipse with it
			Assert.assertTrue(execution instanceof WorkspaceExecution);

			// introspect model?
		} catch (RhenaException ex) {

			throw new Exception(ex);
		}
	}

	private void debugContext(IRhenaEngine engine) throws RhenaException {

		engine.getContext().getCache().getModules().forEach((identifier, module) -> {
			try {
				module.visit(new DebugModelVisitor(engine.getContext(), 0, engine));
			} catch (RhenaException e) {

				e.printStackTrace();
			}
		});
	}
}
