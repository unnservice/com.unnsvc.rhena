
package com.unnsvc.rhena.agent;

import java.io.File;

import org.junit.Test;

import com.unnsvc.rhena.agent.client.AgentClient;
import com.unnsvc.rhena.agent.server.AgentServerProcess;

public class TestAgentClient {

	@Test
	public void testAgentClient() throws Exception {

		String classpath = new File("target/classes").getCanonicalFile().getAbsolutePath();

		AgentClient client = new AgentClient(AgentServerProcess.AGENT_EXECUTION_PORT, classpath);
		client.startup();

		client.shutdown();
	}
}
