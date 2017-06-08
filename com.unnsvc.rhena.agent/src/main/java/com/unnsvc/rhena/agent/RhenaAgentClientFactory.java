
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.ObjectServerException;

public class RhenaAgentClientFactory implements IRhenaAgentClientFactory {

	@Override
	public IRhenaAgentClient newClient(SocketAddress address, int timeout) throws RhenaException {

		try {

			return new RhenaAgentClient(address, timeout);
		} catch (ObjectServerException ose) {
			throw new RhenaException(ose);
		}
	}

}
