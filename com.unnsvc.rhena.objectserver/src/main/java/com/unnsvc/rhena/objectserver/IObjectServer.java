
package com.unnsvc.rhena.objectserver;

import java.net.SocketAddress;

public interface IObjectServer<T extends IObjectServerAcceptor<? extends IObjectRequest, ? extends IObjectReply>> {

	public T newAcceptor();

	public void startServer() throws ObjectServerException;

	public SocketAddress getServerAddress();

	public void close() throws ObjectServerException;

}
