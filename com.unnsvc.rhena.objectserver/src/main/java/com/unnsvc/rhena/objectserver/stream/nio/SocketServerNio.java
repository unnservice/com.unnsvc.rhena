
package com.unnsvc.rhena.objectserver.stream.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.ConnectionException;

public class SocketServerNio implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ExecutorService executor;
	private Selector selector;
	private ServerSocketChannel serverChannel;

	public SocketServerNio() {

		int procs = Runtime.getRuntime().availableProcessors();
		// at least one main thread and one worker thread need to be active
		int moreThanTwo = procs < 2 ? 2 : procs;
		this.executor = Executors.newFixedThreadPool(moreThanTwo);
	}

	public void start(SocketAddress endpoint) throws ConnectionException {

		try {

			selector = SelectorProvider.provider().openSelector();
			serverChannel = SelectorProvider.provider().openServerSocketChannel();
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			serverChannel.bind(endpoint);
			executor.submit(this);
		} catch (Exception ex) {

			throw new ConnectionException(ex);
		}
	}

	@Override
	public Void call() throws Exception {

		// main loop
		while (serverChannel.isOpen()) {

			selector.select();
			Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
			while (selectedKeys.hasNext()) {
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();

				if (!key.isValid()) {
					log.info("Invalid");
					continue;
				}

				ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

				if (key.isAcceptable()) {

					log.info("Accepting");
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					socketChannel.socket().setTcpNoDelay(true);
					// configure client connection...
					socketChannel.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					log.info("Reading");
				} else if (key.isWritable()) {
					log.info("Writing");
				} else {
					log.info("Unknown...");
				}
			}
		}

		return null;
	}

	public void stop() throws ConnectionException {

		try {
			selector.close();
			serverChannel.close();
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | IOException e) {

			throw new ConnectionException(e);
		}
	}

	public ServerSocket getSocket() {

		return serverChannel.socket();
	}

}
