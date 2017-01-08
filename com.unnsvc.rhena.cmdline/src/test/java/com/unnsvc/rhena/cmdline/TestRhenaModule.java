
package com.unnsvc.rhena.cmdline;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.events.LogEvent;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.DebugModelVisitor;

public class TestRhenaModule {

	@Test
	public void testModule() throws Exception {

		IRhenaContext context = new RhenaContext();
		context.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../../")));
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../")));
		context.setLocalRepository(new LocalCacheRepository(context));
		context.setRunTest(true);
		context.setRunItest(true);
		context.setParallel(false);
		// Produce packages or use exploded compilation
		context.setPackageWorkspace(false);
		context.setInstallLocal(true);
		// context.setLogHandler(IRhenaLogHandler logHandler);
		// context.getRepositoryConfiguration().addRepository()
		// context.getRepositoryConfiguration().setProxyXX?
		// context.getTestConfiguration().setXXX
		// context.addListener...
		context.getListenerConfig().addListener(new IContextListener<LogEvent>() {

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

			IRhenaEngine engine = new RhenaEngine(context);

			IRhenaModule entryPointModule = engine.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:core:0.0.1"));
			Assert.assertNotNull(entryPointModule);

			debugContext(engine);

			IRhenaExecution execution = engine.materialiseExecution(entryPointModule, EExecutionType.TEST);
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
				module.visit(new DebugModelVisitor(engine.getContext(), 0, engine));
			} catch (RhenaException e) {

				e.printStackTrace();
			}
		});
	}
}
