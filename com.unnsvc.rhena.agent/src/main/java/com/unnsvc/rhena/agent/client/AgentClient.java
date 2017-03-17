
package com.unnsvc.rhena.agent.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.process.IProcessListener;

/**
 * Responsible for spawning and managing the agent process
 * 
 * @author noname
 *
 */
public class AgentClient extends AbstractAgentClient {

	public AgentClient(int agentExecutionPort, String classpath, List<IProcessListener> agentProcessListeners) {

		super(agentExecutionPort, classpath, agentProcessListeners);
	}

	public AgentClient(int agentExecutionPort, String classpath) {

		super(agentExecutionPort, classpath, new ArrayList<IProcessListener>());
	}

	public AgentClient(int port) {

		this(port, System.getProperty("java.class.path"));
	}

	@Override
	public ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable)
			throws RhenaException {

		ExecutionRequest request = new ExecutionRequest(cache, config, caller, lifecycleExecutable);

		try {
			Socket agentConnection = newAgentExecutionConnection(AgentServerProcess.AGENT_EXECUTION_PORT);

			ObjectOutputStream oos = new ObjectOutputStream(agentConnection.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(agentConnection.getInputStream());
			oos.writeObject(request);
			Object result = ois.readObject();
			agentConnection.close();
			return (ILifecycleExecutionResult) result;
		} catch (IOException | ClassNotFoundException ioe) {

			throw new RhenaException(ioe);
		}
	}

	private Socket newAgentExecutionConnection(int agentExecutionPort) throws IOException {

		long maxWaitMs = 3000;
		long start = System.currentTimeMillis();
		while (true) {

			try {
				SocketChannel channel = SocketChannel.open();
				channel.configureBlocking(true);
				channel.connect(new InetSocketAddress("localhost", agentExecutionPort));
				System.out.println("client: Established agent execution connection");
				return channel.socket();
			} catch (ConnectException ce) {
				if ((System.currentTimeMillis() - start) > maxWaitMs) {
					throw new ConnectException("Connect timeout reached, failed to connect to server");
				}
			}
		}
	}
}
