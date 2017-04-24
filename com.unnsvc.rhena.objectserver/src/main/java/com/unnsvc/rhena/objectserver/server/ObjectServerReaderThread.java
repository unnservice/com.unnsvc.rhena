
package com.unnsvc.rhena.objectserver.server;

import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;

public class ObjectServerReaderThread implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ServerSocketChannel executionChannel;
	private IObjectServerAcceptor serverAcceptor;
	private ExecutorService acceptorPool;

	public ObjectServerReaderThread(ServerSocketChannel executionChannel, IObjectServerAcceptor serverAcceptor) {

		this.executionChannel = executionChannel;
		this.serverAcceptor = serverAcceptor;
		this.acceptorPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@Override
	public Void call() throws Exception {

		/**
		 * Notify after a wait() in the calling object because this object was
		 * called inside a thread pool
		 */
		synchronized (executionChannel) {

			log.info("Object server is listening for connections");
			executionChannel.notifyAll();
		}

		while (executionChannel.isOpen()) {

			SocketChannel clientChannel = executionChannel.accept();
			log.debug("Accepted client execution connection");

			Socket clientSocket = clientChannel.socket();
			clientSocket.setSoTimeout(serverAcceptor.getServerSocketReadTimeout());
			ObjectServerAcceptThread acceptor = new ObjectServerAcceptThread(clientSocket, serverAcceptor);
			acceptorPool.submit(acceptor);
		}
		return null;
	}

}
