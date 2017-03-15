
package com.unnsvc.rhena.agent.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * The client control thread reads requests for loading classes from the server
 * 
 * @author noname
 *
 */
public class AgentClientControlThread extends Thread {

	private Socket socket;

	public AgentClientControlThread(int port) throws IOException {

		socket = connectToAgentServer(port);

		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {

				try {
					socket.close();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});
	}

	public void run() {

		System.out.println("client: control channel listening for connections from server");
		// configure classloader
		ClassLoader contextClassloader = Thread.currentThread().getContextClassLoader();
		AgentClientClassloader classLoader = new AgentClientClassloader(contextClassloader);

		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			while (socket.isConnected()) {
				Object readObject = ois.readObject();
				throw new UnsupportedOperationException("Not implemented");
			}
		} catch (EOFException eofe) {
			// ignore, occurs when socket is closed and we open an
			// ObjectInputStream on it, because objectinputstream reads the
			// stream header to determine if it's an object stream
		} catch (IOException | ClassNotFoundException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * @TODO debug print after how many tries we connected
	 * @param port
	 * @return
	 * @throws IOException
	 */
	protected Socket connectToAgentServer(int port) throws IOException {

		long maxWaitMs = 3000;
		long start = System.currentTimeMillis();
		while (true) {

			try {
				SocketChannel channel = SocketChannel.open();
				channel.configureBlocking(true);
				channel.connect(new InetSocketAddress("localhost", port));
				System.out.println("client: Agent client control thread established server connection");
				return channel.socket();
			} catch (ConnectException ce) {
				if ((System.currentTimeMillis() - start) > maxWaitMs) {
					throw new ConnectException("Connect timeout reached, failed to connect to server");
				}
			}
		}
	}
}
