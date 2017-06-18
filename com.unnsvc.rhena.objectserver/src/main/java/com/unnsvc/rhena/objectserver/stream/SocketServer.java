
package com.unnsvc.rhena.objectserver.stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

public class SocketServer implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ServerSocket socket;
	private ExecutorService executor;
	private IObjectProtocolHandlerFactory protocolFactory;

	public SocketServer(IObjectProtocolHandlerFactory protocolFactory) {

		this.protocolFactory = protocolFactory;

		// int procs = Runtime.getRuntime().availableProcessors();
		// at least one main thread and one worker thread need to be active
		// int moreThanTwo = procs < 4 ? 4 : procs;
		// this.executor = Executors.newFixedThreadPool(moreThanTwo);
		this.executor = Executors.newCachedThreadPool();
	}

	public void start(SocketAddress endpoint) throws IOException {

		socket = new ServerSocket();
		socket.bind(endpoint);
		log.info("Server started on " + socket.getLocalSocketAddress());
		executor.submit(this);
	}

	@Override
	public Void call() throws Exception {

		Socket clientConnection = null;

		while (!socket.isClosed()) {

			try {
				clientConnection = socket.accept();

				log.info("Accepting connection from " + clientConnection);
				executor.submit(new SocketServerApplicationWorker(clientConnection, protocolFactory));
			} catch (SocketException se) {
				// this will be something thrown in accept()

				log.error(se.getMessage());
			} catch (IOException ioe) {

				log.error(ioe.getMessage(), ioe);
			}
		}

		return null;
	}

	public void stop() throws ConnectionException {

		try {

			socket.close();
			executor.shutdown();
			executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (IOException | InterruptedException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	public ServerSocket getSocket() {

		return socket;
	}

}
