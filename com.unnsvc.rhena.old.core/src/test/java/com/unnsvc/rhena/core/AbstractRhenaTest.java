
package com.unnsvc.rhena.core;

import java.io.File;
import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.agent.AgentClient;
import com.unnsvc.rhena.agent.AgentProcessMain;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.visitors.DebugModelVisitor;
import com.unnsvc.rhena.core.config.RhenaConfiguration;
import com.unnsvc.rhena.core.logging.SystemOutLogListener;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

public abstract class AbstractRhenaTest {

	private IRhenaContext context;

	@Before
	public void before() throws Exception {

		int port = ObjectServerHelper.availablePort();

		new Thread(new Runnable() {

			public void run() {

				try {
					AgentProcessMain.main(port + "");
				} catch (Exception e) {

					throw new RuntimeException(e);
				}
			}
		}).start();


		IRhenaConfiguration config = new RhenaConfiguration();
		config.getBuildConfiguration().setParallel(false);
		config.getRepositoryConfiguration().addWorkspace(new File("src/test/resources/testrepo/"));

		AgentClient agentClient = new AgentClient(new InetSocketAddress(port));
		context = new RhenaContext(config, agentClient);
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

		if (context != null) {
			context.close();
		}
	}

	public IRhenaContext getContext() {

		return context;
	}
}
