
package com.unnsvc.rhena.objectserver.stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

public class SocketServer extends Thread implements ISocketServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ServerSocket socket;
	private ExecutorService clientConnectionExecutor;
	private IObjectProtocolHandlerFactory protocolFactory;

	public SocketServer(IObjectProtocolHandlerFactory protocolFactory) {

		this.protocolFactory = protocolFactory;

		/**
		 * We need numerous threads for each connection, so we manage these
		 * threads with an unbound cached executor so they can all run at once,
		 * and we will be able to await termination on the executor when closing
		 * the server
		 */
		this.clientConnectionExecutor = Executors.newCachedThreadPool();
	}

	@Override
	public void startServer(SocketAddress endpoint) throws ConnectionException {

		try {

			socket = new ServerSocket();
			socket.bind(endpoint);
			log.info("Server started on " + socket.getLocalSocketAddress());
			start();
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	@Override
	public void run() {

		Socket clientConnection = null;

		while (!socket.isClosed()) {

			try {
				clientConnection = socket.accept();

				log.info("Accepting connection from " + clientConnection);
				clientConnectionExecutor.submit(new SocketServerWorker(clientConnection, protocolFactory));
			} catch (SocketException se) {
				// this will be something thrown in accept()

				log.error(se.getMessage());
			} catch (IOException ioe) {

				log.error(ioe.getMessage(), ioe);
			}
		}
	}

	@Override
	public void stopServer() throws ConnectionException {

		try {

			socket.close();
			clientConnectionExecutor.shutdown();
			clientConnectionExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (IOException | InterruptedException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	public ServerSocket getSocket() {

		return socket;
	}

}
