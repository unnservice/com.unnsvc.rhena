package com.unnsvc.rhena.agent;

import org.junit.Test;

import com.unnsvc.rhena.agent.client.AgentClient;

public class TestAgentClient {

	@Test
	public void testAgentClient() throws Exception {
		
		AgentClient client = new AgentClient();
		client.startup();
		
		
		
		client.shutdown();
	}
}
