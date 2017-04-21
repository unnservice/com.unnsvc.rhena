
package com.unnsvc.rhena.core;

import org.junit.After;
import org.junit.Before;

import com.unnsvc.rhena.agent.RhenaAgent;
import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.AbstractRhenaConfiguredTest;

public class AbstractAgentTest extends AbstractRhenaConfiguredTest {

	private IRhenaAgent agent;

	@Before
	public void before() throws RhenaException {

		/**
		 * Agent test requires a running agent, so we set up the agent before
		 * the test and tear it down afterward
		 */
		agent = new RhenaAgent();
		agent.start();
	}

	@After
	public void after() throws RhenaException {

		agent.close();
	}
}
