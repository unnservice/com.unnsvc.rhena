
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.agent.client.AgentClient;
import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.visitors.DebugModelVisitor;
import com.unnsvc.rhena.core.config.RhenaConfiguration;
import com.unnsvc.rhena.core.logging.SystemOutLogListener;

public abstract class AbstractRhenaTest {

	private IRhenaContext context;
	private AgentClient agent;

	@Before
	public void before() throws Exception {

		IRhenaConfiguration config = new RhenaConfiguration();
		config.getBuildConfiguration().setParallel(false);
		config.getRepositoryConfiguration().addWorkspace(new File("src/test/resources/testrepo/"));

		context = new RhenaContext(config);
		context.getListenerConfig().addListener(new SystemOutLogListener());

		agent = new AgentClient(AgentServerProcess.AGENT_EXECUTION_PORT);
		agent.startup();
		
		context.setAgent(agent);
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

		agent.shutdown();
		context.close();
	}

	public IRhenaContext getContext() {

		return context;
	}
}
