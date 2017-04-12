
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;

public class ObjectServer implements IObjectServer {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress serverAddress;
	private ExecutorService acceptorPool;
	private ServerSocketChannel executionChannel;

	/**
	 * Starts an object server on a random port
	 * 
	 * @throws RhenaException
	 */
	public ObjectServer(SocketAddress serverAddress) throws RhenaException {

		this.serverAddress = serverAddress;
		System.err.println("Server address: " + serverAddress);

		this.acceptorPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@Override
	public void startServer(IObjectServerAcceptor serverAcceptor) throws RhenaException {

		try {
			executionChannel = ServerSocketChannel.open();
			executionChannel.configureBlocking(true);
			executionChannel.socket().bind(serverAddress);

			while (executionChannel.isOpen()) {

				SocketChannel clientChannel = executionChannel.accept();
				System.out.println("server: Accepted client execution connection");

				Socket clientSocket = clientChannel.socket();
				clientSocket.setSoTimeout(serverAcceptor.getSocketReadTimeout());
				ObjectServerAcceptThread acceptor = new ObjectServerAcceptThread(clientSocket, serverAcceptor);
				acceptorPool.submit(acceptor);
			}

		} catch (AsynchronousCloseException ce) {
			// server connection shutdown while waiting in accept(), no-op
		} catch (IOException ex) {

			throw new RhenaException("Object server entered failed state, not accepting more requests", ex);
		}
	}

	@Override
	public void close() throws RhenaException {

		try {
			executionChannel.close();
			// there will be acceptors in blocking state waiting for object
			// reads, so we can't wait for them
			// acceptorPool.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

	@Override
	public SocketAddress getServerAddress() {

		return serverAddress;
	}
}
