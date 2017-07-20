
package com.unnsvc.rhena.agent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.objectserver.messages.IResponse;
import com.unnsvc.rhena.objectserver.messages.PingRequest;
import com.unnsvc.rhena.objectserver.messages.PingResponse;

public class TestRhenaAgent {

	private IRhenaAgent agentServer;
	private IRhenaAgentClient agentClient;

	@Before
	public void before() throws Exception {

		SocketAddress endpoint = new InetSocketAddress("localhost", 6666);

		agentServer = new RhenaAgentServer(endpoint);
		agentServer.startAgent();
		agentClient = new RhenaAgentClient(endpoint);
	}

	@After
	public void after() throws Exception {

		agentServer.close();
	}

	@Test
	public void testServerProtocol() throws Exception {

		IResponse response = agentClient.submitRequest(new PingRequest());
		Assert.assertTrue(response instanceof PingResponse);
	}
}
