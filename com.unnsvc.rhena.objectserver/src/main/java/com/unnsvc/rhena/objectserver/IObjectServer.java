
package com.unnsvc.rhena.objectserver;

import java.net.SocketAddress;

public interface IObjectServer {

	public void startServer(SocketAddress address) throws ObjectServerException;

	public void stopServer() throws ObjectServerException;
}
