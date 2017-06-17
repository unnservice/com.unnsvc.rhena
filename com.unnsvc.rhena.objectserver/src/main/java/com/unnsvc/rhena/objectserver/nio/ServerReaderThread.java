
package com.unnsvc.rhena.objectserver.nio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import com.unnsvc.rhena.objectserver.nio.messages.IRequest;
import com.unnsvc.rhena.objectserver.nio.messages.IResponse;

public class ServerReaderThread implements Callable<Void> {

	private SocketChannel clientChannel;

	public ServerReaderThread(SocketChannel clientChannel) {

		this.clientChannel = clientChannel;
	}

	@Override
	public Void call() throws Exception {

		System.err.println("Process request and send response");

		Socket socket = clientChannel.socket();
		System.err.println("Opening streams");
		try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {

			System.err.println("Client sending requests");
			IRequest request = (IRequest) ois.readObject();
			oos.writeObject(new IResponse() {
			});
		}

		return null;
	}
}
