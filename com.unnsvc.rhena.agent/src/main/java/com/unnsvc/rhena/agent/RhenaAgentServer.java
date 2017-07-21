
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.agent.protocol.ProtocolHandlerFactory;
import com.unnsvc.rhena.common.IRhenaAgentServer;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.ObjectServer;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandlerFactory;

public class RhenaAgentServer implements IRhenaAgentServer {

	private SocketAddress endpoint;
	private IObjectServer objectServer;

	public RhenaAgentServer(SocketAddress endpoint) {

		this.endpoint = endpoint;
	}

	@Override
	public void startupAgent() throws RhenaException {

		IProtocolHandlerFactory handlerFactory = new ProtocolHandlerFactory(this);
		objectServer = new ObjectServer(handlerFactory);

		try {

			objectServer.startServer(endpoint);
		} catch (ObjectServerException e) {

			throw new RhenaException(e);
		}
	}

	@Override
	public void shutdownAgent() throws RhenaException {

		try {

			objectServer.stopServer();
		} catch (ObjectServerException e) {

			throw new RhenaException(e);
		}
	}
}
