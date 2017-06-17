
package com.unnsvc.rhena.objectserver.old.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.old.IObjectReply;
import com.unnsvc.rhena.objectserver.old.IObjectRequest;
import com.unnsvc.rhena.objectserver.old.IObjectServer;
import com.unnsvc.rhena.objectserver.old.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.old.ObjectServerException;

public abstract class ObjectServer<T extends IObjectServerAcceptor<? extends IObjectRequest, ? extends IObjectReply>> implements IObjectServer<T> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress serverAddress;
	private ExecutorService mainPool;
	private ServerSocketChannel executionChannel;

	/**
	 * Starts an object server on a random port
	 * 
	 * @throws RhenaException
	 */
	public ObjectServer(SocketAddress serverAddress) throws ObjectServerException {

		this.serverAddress = serverAddress;
		log.debug("Object server address: " + serverAddress);

		this.mainPool = Executors.newSingleThreadExecutor();
	}

	@Override
	public abstract T newAcceptor();

	@Override
	public void startServer() throws ObjectServerException {

		try {
			executionChannel = ServerSocketChannel.open();

			/**
			 * The notifyAll() is called inside ObjectServerReaderThread
			 */
			synchronized (executionChannel) {
				executionChannel.configureBlocking(true);
				executionChannel.socket().bind(serverAddress);

				ObjectServerReaderThread reader = new ObjectServerReaderThread(executionChannel, newAcceptor());
				mainPool.submit(reader);

				/**
				 * ObjectServerReaderThread calls notifyAll on executionChannel,
				 * so caller of ObjectServer waits in this startServer method
				 * until the acceptor is up and running
				 **/
				executionChannel.wait();
			}

		} catch (IOException | InterruptedException ex) {

			throw new ObjectServerException("Object server entered failed state, not accepting more requests", ex);
		}
	}

	@Override
	public void close() throws ObjectServerException {

		try {
			// synchronized (executionChannel) {
			executionChannel.close();
			// }
			// there will be acceptors in blocking state waiting for object
			// reads, so we can't wait for them
			// acceptorPool.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe.getMessage(), ioe);
		}
	}

	@Override
	public SocketAddress getServerAddress() {

		return serverAddress;
	}
}
