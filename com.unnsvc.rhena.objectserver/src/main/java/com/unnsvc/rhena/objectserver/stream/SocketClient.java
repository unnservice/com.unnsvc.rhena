
package com.unnsvc.rhena.objectserver.stream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public class SocketClient {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket socket;

	public SocketClient() {

	}

	public void connect(SocketAddress endpoint) throws ConnectionException {

		try {
			log.info("Connecting to " + endpoint);
			socket = new Socket();
			socket.setSoTimeout(1000);
			socket.connect(endpoint);

			log.info("Connected");
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	public void stop() throws ConnectionException {

		
		log.info("Closing socket");
		try {

			if (socket != null) {
				socket.close();
			}
		} catch (IOException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	public IResponse sendRequest(IRequest request) throws ConnectionException {

		log.info("Sending request");

		try {
			return _sendRequest(request);
		} catch (IOException | ClassNotFoundException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	private IResponse _sendRequest(IRequest request) throws IOException, ClassNotFoundException {

		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		log.info("Opened output stream");
		oos.writeObject(request);
		
		// necessary?
		oos.flush();
		socket.getOutputStream().flush();

		log.info("Sent request");

		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		log.info("Opened input stream");
		IResponse response = (IResponse) ois.readObject();
		
		log.info("Response " + response);
		return response;
	}

}
