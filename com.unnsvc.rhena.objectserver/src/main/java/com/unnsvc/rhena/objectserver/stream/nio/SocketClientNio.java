
package com.unnsvc.rhena.objectserver.stream.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.ConnectionException;
import com.unnsvc.rhena.objectserver.stream.TestRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public class SocketClientNio implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Selector selector;
	private SocketChannel socketChannel;

	public SocketClientNio() {

	}

	public void connect(SocketAddress endpoint) throws ConnectionException {

		try {
			selector = SelectorProvider.provider().openSelector();
			socketChannel = SelectorProvider.provider().openSocketChannel();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			socketChannel.connect(endpoint);
			log.info("Connected");
		} catch (IOException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	@Override
	public Void call() throws Exception {

		while (socketChannel.isConnected()) {

			selector.select();
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {

				SelectionKey key = iter.next();
				iter.remove();

				if (key.isConnectable()) {
					
					log.info("Was connectable");
					key.channel().register(selector, SelectionKey.OP_WRITE);
				} else if (key.isWritable()) {

					log.info("Was writable");
				}
			}
		}

		return null;
	}

	public void stop() throws ConnectionException {

	}

	public IResponse sendRequest(TestRequest testRequest) {

		
		return null;
	}
}
