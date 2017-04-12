
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.objectserver.IObjectReply;
import com.unnsvc.rhena.objectserver.IObjectRequest;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.ObjectServerException;

public class ObjectServerAcceptThread implements Runnable {

	private Socket clientSocket;
	private IObjectServerAcceptor serverAcceptor;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectServerAcceptThread(Socket clientSocket, IObjectServerAcceptor serverAcceptor) throws ObjectServerException {

		this.clientSocket = clientSocket;
		this.serverAcceptor = serverAcceptor;

		try {
			this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
			this.ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe);
		}
	}

	@Override
	public void run() {

		try {
			/**
			 * This doesn't need to run in a classloader as the agent has access
			 * to the core classes.
			 */
			while (clientSocket.isConnected()) {

				IObjectRequest request = (IObjectRequest) ois.readObject();
				IObjectReply reply = serverAcceptor.onRequest(request);
				oos.writeObject(reply);
			}

			// we close it after connection exists
			close();
		} catch (IOException | ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void close() throws IOException {

		if (oos != null) {
			oos.close();
		}
		if (ois != null) {
			ois.close();
		}
	}

}
