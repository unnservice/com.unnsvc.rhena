
package com.unnsvc.rhena.objectserver.stream.nio;

import java.net.ServerSocket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServerNioWorker implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ServerSocket socket;

	public SocketServerNioWorker(ServerSocket socket) {

		this.socket = socket;
	}

	@Override
	public Void call() throws Exception {

		
		log.info("Processing request");
		return null;
	}

}
