
package com.unnsvc.rhena.objectserver.stream;

import java.net.SocketAddress;

public interface ISocketServer {

	public void startServer(SocketAddress endpoint) throws ConnectionException;

	public void stopServer() throws ConnectionException;

}
