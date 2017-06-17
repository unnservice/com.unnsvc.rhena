
package com.unnsvc.rhena.objectserver.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConnectionThread extends AbstractConnectionThread<ServerSocketChannel> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private ExecutorService executor;

	public ServerConnectionThread(ExecutorService executor) {

		this.executor = executor;
	}

	public ServerSocketChannel start(SocketAddress endpoint) throws ConnectionException {

		try {
			selector = SelectorProvider.provider().openSelector();

			serverChannel = SelectorProvider.provider().openServerSocketChannel();
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

			if (endpoint == null) {

				serverChannel.bind(null);
			} else {

				serverChannel.bind(endpoint);
			}

			executor.submit(this);
			return serverChannel;
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	@Override
	public Void call() throws Exception {

		log.info("Entering read loop");
		while (serverChannel.isOpen()) {

			log.info("Selecting");
			int selected = selector.select();
			log.info("Selected " + selected);
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {

				SelectionKey key = iter.next();
				iter.remove();
				ServerSocketChannel connectedChannel = (ServerSocketChannel) key.channel();
				// note: connectedChannel is serverChannel
				log.info("Connected channel " + connectedChannel);

				if (!key.isValid()) {
					log.info("Dropping invalid key");
					continue;
				}

				if (key.isAcceptable()) {

					log.info("Channel is acceptable, accepting");
					SocketChannel clientChannel = connectedChannel.accept();
					clientChannel.configureBlocking(false);
					clientChannel.register(selector, SelectionKey.OP_READ);
				}

				if (key.isReadable()) {

					log.info("Channel is readable, reading");
					executor.submit(new ServerReaderThread((SocketChannel) key.channel()));
				} 

				if (key.isWritable()) {

					// check if there's an attachment
					log.info("Channel is readable, writing");
				}
			}
		}

		log.info("Exiting server loop");

		return null;
	}

	public ServerSocketChannel getServerChannel() {

		return serverChannel;
	}

	@Override
	public void close() throws ConnectionException {

		try {

			serverChannel.close();
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}
}
