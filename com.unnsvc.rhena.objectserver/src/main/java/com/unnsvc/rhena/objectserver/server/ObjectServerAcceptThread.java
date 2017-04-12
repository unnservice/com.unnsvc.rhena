
package com.unnsvc.rhena.objectserver.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.IReply;
import com.unnsvc.rhena.objectserver.IRequest;

public class ObjectServerAcceptThread implements Runnable {

	private Socket clientSocket;
	private IObjectServerAcceptor serverAcceptor;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectServerAcceptThread(Socket clientSocket, IObjectServerAcceptor serverAcceptor) throws RhenaException {

		this.clientSocket = clientSocket;
		this.serverAcceptor = serverAcceptor;

		try {
			this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
			this.ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException ioe) {
			throw new RhenaException(ioe);
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

				IRequest request = (IRequest) ois.readObject();
				IReply reply = serverAcceptor.onRequest(request);
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
