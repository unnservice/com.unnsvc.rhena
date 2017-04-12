
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

	public ObjectClient(SocketAddress socketAddress) throws RhenaException {

		establishConnection(socketAddress);
	}

	private void establishConnection(SocketAddress socketAddress) throws RhenaException {

		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(true);
			channel.connect(socketAddress);
			clientSocket = channel.socket();
		} catch (IOException ioe) {
			throw new RhenaException(ioe.getMessage(), ioe);
		}
	}

	@Override
	public IReply executeRequest(IRequest request) throws RhenaException {

		try {
			try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
				try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

					oos.writeObject(request);

					IReply reply = (IReply) ois.readObject();
					return reply;
				}
			}
		} catch (IOException | ClassNotFoundException ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

}
