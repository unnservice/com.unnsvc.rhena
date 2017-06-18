
package com.unnsvc.rhena.objectserver.stream;

import java.net.SocketAddress;

public interface ISocketServer {

	public void start(SocketAddress endpoint) throws ConnectionException;

	public void stop() throws ConnectionException;

}
