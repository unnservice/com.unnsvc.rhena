
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.agent.requests.ExceptionReply;
import com.unnsvc.rhena.agent.requests.LifecycleExecutionRequest;
import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.objectserver.IObjectReply;
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

			IObjectReply reply = executeRequest(request);
			if (reply instanceof ExceptionReply) {
				ExceptionReply exRe = (ExceptionReply) reply;
				RhenaException re = exRe.getException();
				throw re;
			} else if (reply instanceof ILifecycleExecutionResult) {
				return (ILifecycleExecutionResult) executeRequest(request);
			} else {
				throw new ObjectServerException("Unknown reply type: " + reply);
			}
		} catch (ObjectServerException ose) {
			throw new RhenaException(ose);
		}
	}

}
