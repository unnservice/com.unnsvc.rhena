
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.agent.requests.LifecycleExecutionRequest;
import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.client.ObjectClient;

public class AgentClient extends ObjectClient implements IAgentClient {

	public AgentClient(SocketAddress socketAddress) throws ObjectServerException {

		super(socketAddress);
	}

	@Override
	public ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable)
			throws RhenaException {

		LifecycleExecutionRequest request = new LifecycleExecutionRequest(cache, config, caller, lifecycleExecutable);

		try {
			return (ILifecycleExecutionResult) executeRequest(request);
		} catch (ObjectServerException ose) {
			throw new RhenaException(ose);
		}
	}

}
