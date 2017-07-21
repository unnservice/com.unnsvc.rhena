
package com.unnsvc.rhena.objectserver.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public class ObjectClient<REQUEST extends IRequest, RESPONSE extends IResponse> implements IObjectClient<REQUEST, RESPONSE> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress address;

	public ObjectClient(SocketAddress address) {

		this.address = address;
	}

	@Override
	public RESPONSE submitRequest(REQUEST request) throws ObjectServerException {

		try (Socket socket = new Socket()) {
			socket.setSoTimeout(1000);
			socket.connect(address);
			log.debug("Connected to: " + address);

			return submitRequest(socket, request);
		} catch (IOException | ClassNotFoundException ioe) {

			throw new ObjectServerException(ioe);
		}
	}

	@SuppressWarnings("unchecked")
	private RESPONSE submitRequest(Socket socket, REQUEST request) throws IOException, ClassNotFoundException {

		try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

			oos.writeObject(request);
			log.debug("Write request: " + request.getClass().getName());

			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

				Object responseObject = ois.readObject();
				log.debug("Received response: " + responseObject.getClass().getName());
				return (RESPONSE) responseObject;
			}
		}
	}
}
