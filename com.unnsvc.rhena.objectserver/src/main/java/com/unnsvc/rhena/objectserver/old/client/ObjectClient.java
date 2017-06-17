
package com.unnsvc.rhena.objectserver.old.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.old.IObjectClient;
import com.unnsvc.rhena.objectserver.old.IObjectReply;
import com.unnsvc.rhena.objectserver.old.IObjectRequest;
import com.unnsvc.rhena.objectserver.old.ObjectServerException;

public class ObjectClient<REQUEST extends IObjectRequest, REPLY extends IObjectReply> implements IObjectClient<REQUEST, REPLY> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketChannel clientChannel;
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ObjectClient(SocketAddress socketAddress, int timeout) throws ObjectServerException {

		try {

			log.debug("Object client connecting to: " + socketAddress + ", timeout " + timeout);
			establishConnection(socketAddress, timeout);
			log.debug("Established connection to agent server at " + socketAddress);
		} catch (ConnectException ex) {

			throw new ObjectServerException(ex);
		}
	}

	private void establishConnection(SocketAddress socketAddress, int timeout) throws ObjectServerException, ConnectException {

		try {
			clientChannel = SocketChannel.open();
			clientSocket = clientChannel.socket();
			clientSocket.setSoTimeout(timeout);
//			clientSocket.setSoTimeout(0);
			clientChannel.configureBlocking(true);
			
			clientChannel.connect(socketAddress);

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

			log.trace("Submitting to agent " + request);
			oos.writeObject(request);

			log.trace("Returning from writeObject(), now readObject() from server");
			REPLY reply = (REPLY) ois.readObject();
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
			if (clientChannel != null) {
				clientChannel.close();
			}
		} catch (IOException ioe) {
			throw new ObjectServerException(ioe);
		}
	}

}
