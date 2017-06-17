
package com.unnsvc.rhena.objectserver.stream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.ExceptionResponse;
import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

public class SocketServerWorker implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket clientConnection;
	private IObjectProtocolHandlerFactory protocolFactory;

	public SocketServerWorker(Socket clientConnection, IObjectProtocolHandlerFactory protocolFactory) {

		this.clientConnection = clientConnection;
		this.protocolFactory = protocolFactory;
	}

	@Override
	public Void call() throws Exception {

		log.info("call()");

		ObjectInputStream ois = new ObjectInputStream(clientConnection.getInputStream());

		log.info("Processing request");
		Serializable read = (Serializable) ois.readObject();

		try {
			IObjectProtocolHandler objectProtocolHandler = protocolFactory.newObjectProtocolHandler();
			IRequest response = objectProtocolHandler.handleRequest(read);

			ObjectOutputStream oos = new ObjectOutputStream(clientConnection.getOutputStream());
			log.info("Writing reply");
			oos.writeObject(response);
		} catch (Exception ex) {
			
			// submit reply
			
			ObjectOutputStream oos = new ObjectOutputStream(clientConnection.getOutputStream());
			log.info("Writing exception reply");
			oos.writeObject(new ExceptionResponse(ex));
		}

		return null;
	}
}
