
package com.unnsvc.rhena.objectserver.stream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public class SocketClient implements ISocketClient {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket socket;
	private Socket controlSocket;

	public SocketClient() {

	}

	@Override
	public void connect(SocketAddress endpoint) throws ConnectionException {

		try {
			log.info("Connecting to " + endpoint);
			socket = new Socket();
			socket.setSoTimeout(1000);
			socket.connect(endpoint);

			controlSocket = new Socket();
			controlSocket.setSoTimeout(500);
			controlSocket.connect(endpoint);
			SocketServerControlWorker pingerThread = new SocketServerControlWorker(this) {

				@Override
				public void onFailure() {

					log.error("No reply from server, entering failed state... Aborting connections.");
				}
			};
			pingerThread.startTimer(1000);

			log.info("Connected");
		} catch (IOException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	@Override
	public void stop() throws ConnectionException {

		log.info("Closing socket");
		try {

			if (socket != null) {
				socket.close();
			}

			if (controlSocket != null) {
				controlSocket.close();
			}
		} catch (IOException ioe) {
			throw new ConnectionException(ioe);
		}
	}

	@Override
	public IResponse sendRequest(IRequest request, ERequestChannel channel) throws ConnectionException {

		log.info("Sending request");

		Socket currentSocket = this.socket;

		switch (channel) {
			case APPLICATION:
				currentSocket = this.socket;
				break;
			case CONTROL:
				currentSocket = this.controlSocket;
				break;
			default:
				throw new ConnectionException("Unknown channel");
		}

		try {
			return _sendRequest(request, currentSocket);
		} catch (SocketTimeoutException ste) {

			throw new ConnectionTimeoutException(ste);
		} catch (IOException | ClassNotFoundException ioe) {

			throw new ConnectionException(ioe);
		}
	}

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private IResponse _sendRequest(IRequest request, Socket socket) throws IOException, ClassNotFoundException {

		if (oos == null) {
			oos = new ObjectOutputStream(socket.getOutputStream());
			log.info("Opened output stream");
		}

		if (ois == null) {
			ois = new ObjectInputStream(socket.getInputStream());
			log.info("Opened input stream");
		}

		oos.writeObject(request);

		// necessary?
		oos.flush();
		socket.getOutputStream().flush();

		log.info("Sent request");

		IResponse response = (IResponse) ois.readObject();

		log.info("Response " + response);
		return response;
	}

}
