
package com.unnsvc.rhena.objectserver.stream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServerWorker implements Callable<Void> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Socket clientConnection;

	public SocketServerWorker(Socket clientConnection) {

		this.clientConnection = clientConnection;
	}

	@Override
	public Void call() throws Exception {

		ObjectInputStream ois = new ObjectInputStream(clientConnection.getInputStream());

		log.info("Processing request");
		Object read = ois.readObject();

		ObjectOutputStream oos = new ObjectOutputStream(clientConnection.getOutputStream());

		log.info("Writing reply");
		oos.writeObject(read);

		return null;
	}

}
