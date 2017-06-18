
package com.unnsvc.rhena.objectserver.stream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.ExceptionResponse;
import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

/**
 * This worker manages the streams on the connection and the client connection
 * loop, subclasses manage how protocols are handled
 * 
 * @author noname
 *
 */
public abstract class AbstractSocketServerWorker extends Thread {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket clientConnection;
	private IObjectProtocolHandlerFactory protocolFactory;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public AbstractSocketServerWorker(Socket clientConnection, IObjectProtocolHandlerFactory protocolFactory) {

		this.clientConnection = clientConnection;
		this.protocolFactory = protocolFactory;
	}

	@Override
	public void run() {

		try {
			ois = new ObjectInputStream(clientConnection.getInputStream());

			while (clientConnection.isConnected()) {

				log.info("Processing request");
				IRequest request = (IRequest) ois.readObject();

				onRequest(request);
			}
		} catch (Throwable throwable) {

			log.error(throwable.getMessage(), throwable);
			/**
			 * @TODO close connection
			 */

			try {
				// log.info("Writing exception reply");
				sendReply(new ExceptionResponse(throwable));
			} catch (ConnectionException ce) {
				/**
				 * @TODO close the connection...
				 */
				throw new RuntimeException("Failed to send error response");
			}
		}
	}

	protected abstract void onRequest(IRequest request) throws ConnectionException;

	public void sendReply(IResponse response) throws ConnectionException {

		try {
			oos = new ObjectOutputStream(clientConnection.getOutputStream());
			log.info("Writing exception reply");
			oos.writeObject(response);
			oos.flush();
		} catch (IOException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	public IObjectProtocolHandlerFactory getProtocolFactory() {

		return protocolFactory;
	}

	public Socket getClientConnection() {

		return clientConnection;
	}

}
