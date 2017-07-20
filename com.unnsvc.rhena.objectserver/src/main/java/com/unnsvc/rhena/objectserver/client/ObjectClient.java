
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

public class ObjectClient implements IObjectClient {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress address;

	public ObjectClient(SocketAddress address) {

		this.address = address;
	}

	@Override
	public IResponse submitRequest(IRequest request) throws ObjectServerException {

		try (Socket socket = new Socket()) {
			socket.setSoTimeout(1000);
			socket.connect(address);
			log.debug("Connected to: " + address);

			return submitRequest(socket, request);
		} catch (IOException | ClassNotFoundException ioe) {

			throw new ObjectServerException(ioe);
		}
	}

	private IResponse submitRequest(Socket socket, IRequest request) throws IOException, ClassNotFoundException {

		try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

			oos.writeObject(request);
			log.debug("Write request: " + request.getClass().getName());

			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

				Object responseObject = ois.readObject();
				IResponse response = (IResponse) responseObject;
				log.debug("Received response: " + response.getClass().getName());
				return response;
			}
		}
	}
}
