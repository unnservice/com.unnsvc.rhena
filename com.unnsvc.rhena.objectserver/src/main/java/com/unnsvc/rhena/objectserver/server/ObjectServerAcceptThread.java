
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.IReply;
import com.unnsvc.rhena.objectserver.IRequest;

public class ObjectServerAcceptThread implements Runnable {

	private Socket clientSocket;
	private IObjectServerAcceptor serverAcceptor;

	public ObjectServerAcceptThread(Socket clientSocket, IObjectServerAcceptor serverAcceptor) {

		this.clientSocket = clientSocket;
		this.serverAcceptor = serverAcceptor;
	}

	@Override
	public void run() {

		/**
		 * This doesn't need to run in a classloader as the agent has access to
		 * the core classes.
		 */
		try (ObjectInputStream clientRequests = new ObjectInputStream(clientSocket.getInputStream())) {
			try (ObjectOutputStream clientReplies = new ObjectOutputStream(clientSocket.getOutputStream())) {

				IRequest request = (IRequest) clientRequests.readObject();
				IReply reply = serverAcceptor.onRequest(request);
				clientReplies.writeObject(reply);
			}
		} catch (IOException | ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

}
