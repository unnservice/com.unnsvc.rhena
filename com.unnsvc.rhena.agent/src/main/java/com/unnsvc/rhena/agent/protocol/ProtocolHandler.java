
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.common.IRhenaAgentServer;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;

public class ProtocolHandler implements IProtocolHandler<IExecutionRequest, IExecutionResponse> {

	private IRhenaAgentServer agentServer;

	public ProtocolHandler(IRhenaAgentServer agentServer) {

		this.agentServer = agentServer;
	}

	@Override
	public IExecutionResponse handleRequest(IExecutionRequest request) throws ObjectServerException {

		throw new UnsupportedOperationException("Not implemented");
	}
}
