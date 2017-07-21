
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;

public class RhenaAgentClientFactory implements IRhenaAgentClientFactory {

	@Override
	public IRhenaAgentClient<IExecutionRequest, IExecutionResponse> newClient(SocketAddress endpoint) throws RhenaException {

		return new RhenaAgentClient(endpoint);
	}
}
