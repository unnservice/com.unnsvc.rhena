
package com.unnsvc.rhena.objectserver;

import java.net.SocketAddress;

public interface IObjectServer {

	public void startServer(IObjectServerAcceptor serverAcceptor) throws ObjectServerException;

	public SocketAddress getServerAddress();

	public void close() throws ObjectServerException;

}
