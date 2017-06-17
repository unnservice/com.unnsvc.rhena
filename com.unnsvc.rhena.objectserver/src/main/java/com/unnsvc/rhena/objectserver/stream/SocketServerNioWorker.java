
package com.unnsvc.rhena.objectserver.stream;

import java.net.ServerSocket;
import java.util.concurrent.Callable;

public class SocketServerNioWorker implements Callable<Void> {

	private ServerSocket socket;

	public SocketServerNioWorker(ServerSocket socket) {

		this.socket = socket;
	}

	@Override
	public Void call() throws Exception {

		return null;
	}

}
