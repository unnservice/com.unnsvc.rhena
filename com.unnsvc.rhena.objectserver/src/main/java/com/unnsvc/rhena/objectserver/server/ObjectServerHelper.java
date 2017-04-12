
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.ObjectServerException;

public class ObjectServerHelper {

	public static SocketAddress availableAddress() throws ObjectServerException {

		try {
			ServerSocket s = new ServerSocket(0);
			s.close();
			return s.getLocalSocketAddress();
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe.getMessage(), ioe);
		}
	}
	
	public static int availablePort() throws ObjectServerException {

		try {
			ServerSocket s = new ServerSocket(0);
			s.close();
			return s.getLocalPort();
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe.getMessage(), ioe);
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
	public static void waitObjectServer(IObjectServer objectServer, int connectionTimeoutMs) throws ObjectServerException {

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
