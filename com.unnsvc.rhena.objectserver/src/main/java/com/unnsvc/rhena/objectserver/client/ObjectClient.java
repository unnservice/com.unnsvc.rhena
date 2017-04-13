
package com.unnsvc.rhena.objectserver.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.unnsvc.rhena.objectserver.IObjectClient;
import com.unnsvc.rhena.objectserver.IObjectReply;
import com.unnsvc.rhena.objectserver.IObjectRequest;
import com.unnsvc.rhena.objectserver.ObjectServerException;

public class ObjectClient implements IObjectClient {

	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int maxConnectRetries = 5;

	public ObjectClient(SocketAddress socketAddress) throws ObjectServerException {

		int tries = 0;
		while (true) {
			try {
				establishConnection(socketAddress);
				break;
			} catch (ConnectException ce) {
				tries++;
				if (tries == maxConnectRetries) {

					throw new ObjectServerException("Failed to connect after " + maxConnectRetries + " tries", ce);
				}
			}
		}
	}

	private void establishConnection(SocketAddress socketAddress) throws ObjectServerException, ConnectException {

		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(true);
			channel.connect(socketAddress);
			clientSocket = channel.socket();
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (ConnectException ce) {
			throw ce;
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe);
		}
	}

	@Override
	public IObjectReply executeRequest(IObjectRequest request) throws ObjectServerException {

		try {

			oos.writeObject(request);

			IObjectReply reply = (IObjectReply) ois.readObject();
			return reply;
		} catch (IOException | ClassNotFoundException ex) {
			throw new ObjectServerException(ex.getMessage(), ex);
		}
	}

	@Override
	public void close() throws ObjectServerException {

		try {
			if (oos != null) {
				oos.close();
			}
			if (ois != null) {
				ois.close();
			}
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe);
		}
	}

}
