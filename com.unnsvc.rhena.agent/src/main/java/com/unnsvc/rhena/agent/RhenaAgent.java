
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.old.IObjectServer;
import com.unnsvc.rhena.objectserver.old.ObjectServerException;
import com.unnsvc.rhena.objectserver.old.server.ObjectServer;

public class RhenaAgent implements IRhenaAgent {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IObjectServer<RhenaAgentAcceptor> objectServer;

	public RhenaAgent(SocketAddress agentAddress) throws RhenaException {

		try {

			objectServer = new ObjectServer<RhenaAgentAcceptor>(agentAddress) {

				@Override
				public RhenaAgentAcceptor newAcceptor() {

					log.debug("Creating new RhenaAgentAcceptor");
					return new RhenaAgentAcceptor();
				}
			};
			log.info("Starting agent server on: " + agentAddress);
		} catch (ObjectServerException ose) {

			throw new RhenaException(ose);
		}
	}

	@Override
	public void startAgent() throws RhenaException {

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
