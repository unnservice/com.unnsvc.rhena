
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.visitors.DebugModelVisitor;
import com.unnsvc.rhena.core.config.RhenaConfiguration;
import com.unnsvc.rhena.core.logging.SystemOutLogListener;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

public abstract class AbstractRhenaTest {

	private IRhenaContext context;

	@Before
	public void before() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.setInstanceHome(new File(System.getProperty("user.home"), ".rhena"));
		config.getBuildConfiguration().setParallel(false);
		config.getBuildConfiguration().setPackageWorkspace(true);
		config.getAgentConfiguration().setAgentClasspath(System.getProperty("java.class.path"));

		context = new RhenaContext(config);
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("src/test/resources/testrepo/")));
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
