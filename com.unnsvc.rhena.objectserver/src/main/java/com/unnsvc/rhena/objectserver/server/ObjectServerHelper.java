
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectServer;

public class ObjectServerHelper {

	public static SocketAddress availableAddress() throws RhenaException {

		try {
			ServerSocket s = new ServerSocket(0);
			s.close();
			return s.getLocalSocketAddress();
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Block and wait for object server to become available
	 * 
	 * @param objectServer
	 * @param connectionTimeoutMs
	 *            connection timeout milliseconds
	 * @throws RhenaException
	 */
	public static void waitObjectServer(IObjectServer objectServer, int connectionTimeoutMs) throws RhenaException {

//		try {
//			SocketChannel channel = SocketChannel.open();
//			channel.configureBlocking(true);
//			channel.connect(objectServer.getServerAddress());
//		} catch (IOException ex) {
//			throw new RhenaException(ex.getMessage(), ex);
//		}
		
		throw new UnsupportedOperationException("Not implemented");
	}
}
