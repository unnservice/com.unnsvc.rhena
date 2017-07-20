
package com.unnsvc.rhena.objectserver.ng.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.ng.ObjectServerException;
import com.unnsvc.rhena.objectserver.ng.messages.PingRequest;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

public class ObjectClient implements IObjectClient {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress address;

	public ObjectClient(SocketAddress address) {

		this.address = address;
	}

	public Response submitRequest(PingRequest request) throws ObjectServerException {

		try (Socket socket = new Socket()) {
			socket.setSoTimeout(1000);
			socket.connect(address);
			log.debug("Connected to: " + address);

			return submitRequest(socket, request);
		} catch (IOException | ClassNotFoundException ioe) {

			throw new ObjectServerException(ioe);
		}
	}

	private Response submitRequest(Socket socket, PingRequest request) throws IOException, ClassNotFoundException {

		try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

			oos.writeObject(request);
			log.debug("Write request: " + request.getClass().getName());

			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

				Object responseObject = ois.readObject();
				Response response = (Response) responseObject;
				log.debug("Received response: " + response.getClass().getName());
				return response;
			}
		}
	}
}
