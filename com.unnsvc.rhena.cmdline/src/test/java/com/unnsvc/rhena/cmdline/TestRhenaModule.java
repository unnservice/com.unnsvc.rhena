
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
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.events.LogEvent;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.DebugModelVisitor;

public class TestRhenaModule {

	@Test
	public void testModule() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.addWorkspaceRepository(new WorkspaceRepository(config, new File("../../")));
		config.addWorkspaceRepository(new WorkspaceRepository(config, new File("../")));
		config.setLocalRepository(new LocalCacheRepository(config));
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
		config.getListenerConfig().addListener(new IContextListener<LogEvent>() {

			@Override
			public void onEvent(LogEvent event) throws RhenaException {

				System.out.println(event.toString());
			}

			@Override
			public Class<LogEvent> getType() {

				return LogEvent.class;
			}
		});

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
