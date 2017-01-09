
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.core.logging.SystemOutLogListener;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.DebugModelVisitor;

public abstract class AbstractRhenaTest {

	private IRhenaContext context;

	@Before
	public void before() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(false);
		// Produce packages or use exploded compilation
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);
		// context.setLogHandler(IRhenaLogHandler logHandler);
		// context.getRepositoryConfiguration().addRepository()
		// context.getRepositoryConfiguration().setProxyXX?
		// context.getTestConfiguration().setXXX
		// context.addListener...
		context = new RhenaContext(config);
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("src/test/resources/testrepo/")));
		context.setLocalRepository(new LocalCacheRepository(context));
		context.getListenerConfig().addListener(new SystemOutLogListener());
	}

	protected void debugContext(IRhenaEngine engine) throws RhenaException {

		engine.getContext().getCache().getModules().forEach((identifier, module) -> {
			try {
				module.visit(new DebugModelVisitor(engine.getContext(), 0, engine));
			} catch (RhenaException e) {

				e.printStackTrace();
			}
		});
	}

	@After
	public void after() throws Exception {

		context.close();
	}

	public IRhenaContext getContext() {

		return context;
	}
}
