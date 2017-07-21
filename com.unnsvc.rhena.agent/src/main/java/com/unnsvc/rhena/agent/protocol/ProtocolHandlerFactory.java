
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.common.IRhenaAgentServer;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandlerFactory;

public class ProtocolHandlerFactory implements IProtocolHandlerFactory {

	private IRhenaAgentServer agentServer;

	public ProtocolHandlerFactory(IRhenaAgentServer agentServer) {

		this.agentServer = agentServer;
	}

	@Override
	public IProtocolHandler newProtocolHandler() {

		return new ProtocolHandler(agentServer);
	}
}
