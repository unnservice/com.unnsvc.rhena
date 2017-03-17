
package com.unnsvc.rhena.agent.server;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AgentServerProcess {

	public static final int AGENT_CONTROL_PORT = 14231;
	public static final int AGENT_EXECUTION_PORT = 14232;

	public static void main(String... args) throws Exception {
		
		System.out.println("server: Server agent process start");
		new AgentServerProcess().startServer();
	}

	protected void startServer() throws Exception {

		listenExecutions();
	}

	private void listenExecutions() throws Exception {

		ServerSocketChannel executionChannel = ServerSocketChannel.open();
		executionChannel.configureBlocking(true);
		executionChannel.socket().bind(new InetSocketAddress(AGENT_EXECUTION_PORT));

		SocketChannel clientExecutionConnection = null;
		while ((clientExecutionConnection = executionChannel.accept()) != null) {
			
			System.out.println("server: Accepted client execution connection");
			AgentServerExecutionClassloader agentClassLoader = new AgentServerExecutionClassloader(AgentServerProcess.class.getClassLoader());
			AgentServerExecutionConnection conn = new AgentServerExecutionConnection(clientExecutionConnection.socket());
			conn.setContextClassLoader(agentClassLoader);
			conn.setDaemon(true);
			conn.start();
		}
	}
}
