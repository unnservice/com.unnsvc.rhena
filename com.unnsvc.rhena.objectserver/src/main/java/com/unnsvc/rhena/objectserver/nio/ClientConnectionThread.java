
package com.unnsvc.rhena.objectserver.nio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectionThread extends AbstractConnectionThread<SocketChannel> {

	private Logger log = LoggerFactory.getLogger(getClass());
	// private ExecutorService executor;
	private SocketChannel clientChannel;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ClientConnectionThread(ExecutorService executor) {

		// this.executor = executor;
	}

	public SocketChannel start(SocketAddress endpoint) throws ConnectionException {

		log.info("start");
		try {

			clientChannel = SelectorProvider.provider().openSocketChannel();
			clientChannel.configureBlocking(true);
			log.info("Connecting");
			if (clientChannel.connect(endpoint)) {

				log.info("Connected");
				Socket clientSocket = clientChannel.socket();
				log.info("Socket");
				ois = new ObjectInputStream(clientSocket.getInputStream());
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				log.info("Created object streams");
				
				// executor.submit(this);
			} else {
				throw new IOException("Not connected");
			}
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}

		return null;
	}

	@Override
	public Void call() throws Exception {

		return null;
	}

	public void submit(Serializable serialisable) throws ConnectionException {

		try {

			log.info("Writing object");
			oos.writeObject(serialisable);
			log.info("Reading object");
			Object reply = ois.readObject();

		} catch (ClassNotFoundException | IOException ex) {
			throw new ConnectionException(ex);
		}
	}

	@Override
	public void close() throws ConnectionException {

		try {

			oos.close();
			ois.close();
			clientChannel.close();
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}

}
