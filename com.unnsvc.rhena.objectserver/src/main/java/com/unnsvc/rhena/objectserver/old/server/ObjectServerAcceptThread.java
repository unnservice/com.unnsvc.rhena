
package com.unnsvc.rhena.objectserver.old.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.old.IObjectReply;
import com.unnsvc.rhena.objectserver.old.IObjectRequest;
import com.unnsvc.rhena.objectserver.old.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.old.ObjectServerException;

/**
 * This class is created for each accepted connection.
 * 
 * @author noname
 *
 * @param <REQUEST>
 * @param <REPLY>
 */
public class ObjectServerAcceptThread<REQUEST extends IObjectRequest, REPLY extends IObjectReply> implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket clientSocket;
	private IObjectServerAcceptor serverAcceptor;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectServerAcceptThread(Socket clientSocket, IObjectServerAcceptor serverAcceptor) throws ObjectServerException {

		this.clientSocket = clientSocket;
		this.serverAcceptor = serverAcceptor;
		
		try {
			this.ois = new ObjectInputStream(clientSocket.getInputStream());
			this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
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
				log.trace("Received object from client: " + request);
				IObjectReply reply = serverAcceptor.onRequest(request);
				oos.writeObject(reply);
			}

			// we close it after connection exists
			close();
		} catch (Throwable ex) {
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
