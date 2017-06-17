
package com.unnsvc.rhena.objectserver.old;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

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
}
