
package com.unnsvc.rhena.objectserver;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IObjectServer {

	public void startServer(IObjectServerAcceptor serverAcceptor) throws RhenaException;

	public SocketAddress getServerAddress();

	public void close() throws RhenaException;

}
