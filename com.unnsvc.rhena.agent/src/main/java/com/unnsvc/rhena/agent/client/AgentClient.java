
package com.unnsvc.rhena.agent.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;

/**
 * Responsible for spawning and managing the agent process
 * 
 * @author noname
 *
 */
public class AgentClient extends AbstractAgentClient {

	@Override
	public ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable) {

		Socket agentConnection = newAgentExecutionConnection(AgentServerProcess.AGENT_EXECUTION_PORT);

		try {
			ObjectOutputStream oos = new ObjectOutputStream(agentConnection.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(agentConnection.getInputStream());
			oos.writeObject(lifecycleExecutable);
			Object result = ois.readObject();
			agentConnection.close();
			return (ILifecycleExecutionResult) result;
		} catch (IOException | ClassNotFoundException ioe) {
			
			throw new RuntimeException(ioe.getMessage());
		}
	}

	private Socket newAgentExecutionConnection(int agentExecutionPort) {

		return null;
	}
}
