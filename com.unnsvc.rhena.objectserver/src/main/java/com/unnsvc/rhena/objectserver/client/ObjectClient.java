
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

public class ObjectClient<REQUEST extends IObjectRequest, REPLY extends IObjectReply> implements IObjectClient<REQUEST, REPLY> {

	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectClient(SocketAddress socketAddress) throws ObjectServerException {

		try {

			establishConnection(socketAddress);
		} catch (ConnectException ex) {

			throw new ObjectServerException(ex);
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
	@SuppressWarnings("unchecked")
	public REPLY executeRequest(REQUEST request) throws ObjectServerException {

		try {

			oos.writeObject(request);

			return (REPLY) ois.readObject();
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
