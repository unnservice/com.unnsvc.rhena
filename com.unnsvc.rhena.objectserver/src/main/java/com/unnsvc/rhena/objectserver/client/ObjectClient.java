
package com.unnsvc.rhena.objectserver.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectClient;
import com.unnsvc.rhena.objectserver.IReply;
import com.unnsvc.rhena.objectserver.IRequest;

public class ObjectClient implements IObjectClient {

	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectClient(SocketAddress socketAddress) throws RhenaException {

		establishConnection(socketAddress);
	}

	private void establishConnection(SocketAddress socketAddress) throws RhenaException {

		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(true);
			channel.connect(socketAddress);
			clientSocket = channel.socket();
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

	@Override
	public IReply executeRequest(IRequest request) throws RhenaException {

		try {

			oos.writeObject(request);

			IReply reply = (IReply) ois.readObject();
			return reply;
		} catch (IOException | ClassNotFoundException ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	@Override
	public void close() throws RhenaException {

		try {
			if (oos != null) {
				oos.close();
			}
			if (ois != null) {
				ois.close();
			}
		} catch (IOException ioe) {
			throw new RhenaException(ioe);
		}
	}

}
