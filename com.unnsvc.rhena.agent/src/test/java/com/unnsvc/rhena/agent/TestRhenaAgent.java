
package com.unnsvc.rhena.agent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.agent.messages.PingRequest;
import com.unnsvc.rhena.agent.messages.PingResponse;
import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaAgentServer;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;

public class TestRhenaAgent {

	private IRhenaAgentServer agentServer;
	private IRhenaAgentClient<IExecutionRequest, IExecutionResponse> agentClient;

	@Before
	public void before() throws Exception {

		SocketAddress endpoint = new InetSocketAddress("localhost", 6666);

		agentServer = new RhenaAgentServer(endpoint);
		agentServer.startServer();
		agentClient = new RhenaAgentClient(endpoint);
	}

	@After
	public void after() throws Exception {

		agentServer.close();
	}

	@Test
	public void testServerProtocol() throws Exception {

		IExecutionResponse response = agentClient.submitRequest(new PingRequest());
		Assert.assertTrue(response instanceof PingResponse);
	}
}
