
package com.unnsvc.rhena.common;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaAgentClientFactory {

	public IRhenaAgentClient newClient(SocketAddress endpoint) throws RhenaException;

}
