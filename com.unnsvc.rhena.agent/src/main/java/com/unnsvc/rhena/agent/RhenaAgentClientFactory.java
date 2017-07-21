
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaAgentClientFactory implements IRhenaAgentClientFactory {

	@Override
	public IRhenaAgentClient newClient(SocketAddress endpoint) throws RhenaException {

		return new RhenaAgentClient(endpoint);
	}
}
