
package com.unnsvc.rhena.agent;

import java.io.Serializable;
import java.net.SocketAddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

public class TestRhenaAgent implements Serializable {

	private static final long serialVersionUID = 1L;
	private SocketAddress address;
	private Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void before() throws ObjectServerException, RhenaException, InterruptedException {

		address = ObjectServerHelper.availableAddress();
		RhenaAgent agent = new RhenaAgent(address);
		agent.startAgent();
	}

	@Test
	public void testAgent() throws Exception {

		RhenaAgentClientFactory fact = new RhenaAgentClientFactory();
		log.debug("Address " + address);
		try (IRhenaAgentClient client = fact.newClient(address, 1000)) {

			IExecutionResult result = (IExecutionResult) client.executeRequest(new IExecutionRequest() {

				private static final long serialVersionUID = 1L;

				@Override
				public IEntryPoint getEntryPoint() {

					throw new UnsupportedOperationException("Not implemented");
				}

				@Override
				public IRhenaModule getModule() {

					throw new UnsupportedOperationException("Not implemented");
				}

				@Override
				public ILifecycleInstance getLifecycle() {

					throw new UnsupportedOperationException("Not implemented");
				}

				@Override
				public IDependencies getDependencies() {

					throw new UnsupportedOperationException("Not implemented");
				}

			});
			Assert.assertTrue(result instanceof IExecutionResult);
		}
	}
}
