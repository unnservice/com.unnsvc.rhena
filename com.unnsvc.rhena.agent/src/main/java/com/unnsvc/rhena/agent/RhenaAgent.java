
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.server.ObjectServer;

public class RhenaAgent implements IRhenaAgent {

	private IObjectServer<RhenaAgentAcceptor> objectServer;

	public RhenaAgent(SocketAddress agentAddress) throws RhenaException {

		try {
			objectServer = new ObjectServer<RhenaAgentAcceptor>(agentAddress) {

				@Override
				public RhenaAgentAcceptor newAcceptor() {

					return new RhenaAgentAcceptor();
				}
			};
		} catch (ObjectServerException ose) {

			throw new RhenaException(ose);
		}
	}

	@Override
	public void start() throws RhenaException {

		try {
			objectServer.startServer();
		} catch (ObjectServerException ex) {

			throw new RhenaException(ex);
		}
	}

	@Override
	public void close() throws RhenaException {

		try {
			objectServer.close();
		} catch (ObjectServerException ose) {

			throw new RhenaException(ose);
		}
	}
}
