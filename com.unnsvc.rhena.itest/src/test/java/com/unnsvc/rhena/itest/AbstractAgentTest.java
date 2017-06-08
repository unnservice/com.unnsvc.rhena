
package com.unnsvc.rhena.itest;

import java.net.SocketAddress;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.agent.RhenaAgent;
import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;
import com.unnsvc.rhena.core.RhenaFactories;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;
import com.unnsvc.rhena.repository.RhenaResolver;

public abstract class AbstractAgentTest extends AbstractRhenaConfiguredTest {

	private IRhenaConfiguration config;
	private IRhenaContext context;
	private IRhenaAgent agent;

	@Before
	public void before() throws RhenaException {

		try {
			SocketAddress agentAddress = ObjectServerHelper.availableAddress();

			config = createMockConfig();
			config.getAgentConfiguration().setAgentAddress(agentAddress);

			IRhenaResolver resolver = new RhenaResolver();

			IRhenaFactories factories = new RhenaFactories();
			context = createMockContext(config, resolver, factories);

			/**
			 * Agent test requires a running agent, so we set up the agent
			 * before the test and tear it down afterward
			 */
			agent = new RhenaAgent(config.getAgentConfiguration().getAgentAddress());
			agent.startAgent();
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	@After
	public void after() throws RhenaException {

		agent.close();
	}

	public IRhenaContext getContext() {

		return context;
	}
	
	
	public IRhenaConfiguration getConfig() {

		return config;
	}
}
