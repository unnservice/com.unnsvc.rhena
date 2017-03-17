
package com.unnsvc.rhena.agent.server;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AgentServerProcess {

	public static final int AGENT_EXECUTION_PORT = 14232;

	public static void main(String... args) throws Exception {
		
		int executionPort = Integer.valueOf(args[0]);
		
		System.out.println("server: Server agent process start, listening on port " + executionPort);
		new AgentServerProcess().listenExecutions(executionPort);
	}

	private void listenExecutions(int executionPort) throws Exception {

		ServerSocketChannel executionChannel = ServerSocketChannel.open();
		executionChannel.configureBlocking(true);
		executionChannel.socket().bind(new InetSocketAddress(executionPort));

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
