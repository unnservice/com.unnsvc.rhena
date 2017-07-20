
package com.unnsvc.rhena.objectserver.ng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.ng.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.ng.handler.IProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.ng.messages.ExceptionResponse;
import com.unnsvc.rhena.objectserver.ng.messages.Request;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

public class ObjectServer implements IObjectServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ServerSocket server;
	private IProtocolHandlerFactory handlerFactory;
	private ExecutorService executor;

	public ObjectServer(IProtocolHandlerFactory handlerFactory) {

		this.handlerFactory = handlerFactory;
		// Might want to have a look at this to not spawn an endless number of
		// threads
		this.executor = Executors.newCachedThreadPool();
	}

	@Override
	public void startServer(SocketAddress address) throws ObjectServerException {

		try {
			server = new ServerSocket();
			// server.accept() throws timeout exception if this is set
			// server.setSoTimeout(1000);
			server.bind(address);
			log.debug("Server bound to: " + address);

			executor.execute(new Runnable() {

				public void run() {

					try {

						log.debug("Starting server loop");
						startServerLoop(server);
					} catch (IOException ioe) {

						throw new RuntimeException(ioe);
					}
				}
			});

		} catch (IOException ioe) {

			throw new ObjectServerException(ioe);
		}
	}

	private void startServerLoop(ServerSocket server) throws IOException {

		while (!server.isClosed()) {

			try {
				Socket client = server.accept();
				log.debug("Accepted connection");

				/**
				 * Submit for execution
				 */
				executor.execute(new Runnable() {

					public void run() {

						try {

							handleRequest(client);
						} catch (Exception ex) {

							log.error(ex.getMessage(), ex);
						}
					}
				});
			} catch (SocketException se) {

				// If socket is closed, SocketException is most likely thrown
				// from server.accept() which was in blocking state
				if (!server.isClosed()) {

					throw se;
				}
			}
		}
	}

	private void handleRequest(Socket client) throws IOException, ClassNotFoundException, ObjectServerException {

		try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {

			Object requestObject = ois.readObject();
			log.debug("Read request: " + requestObject.getClass().getName());

			if (!(requestObject instanceof Request)) {
				throw new ObjectServerException("Request object not instance of Request: " + requestObject.getClass().getName());
			}

			IProtocolHandler handler = handlerFactory.newProtocolHandler();
			try {
				Response response = handler.handleRequest((Request) requestObject);
				log.debug("Handled request in protocol handler: " + handler.getClass().getName());

				try (ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {

					oos.writeObject(response);
					log.debug("Submitted reply to client");
				}
			} catch (Throwable throwable) {

				if (!client.isClosed()) {
					try (ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {

						oos.writeObject(new ExceptionResponse(throwable));
						log.debug("Submitted exception reply to client");
					}
				} else {
					throw new ObjectServerException(throwable);
				}
			}
		}
	}

	@Override
	public void stopServer() throws ObjectServerException {

		try {

			log.debug("stopServer()");
			server.close();
		} catch (IOException e) {

			throw new ObjectServerException(e);
		}
	}
}
