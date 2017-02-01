
package com.unnsvc.rhena.agent.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomServerSocket extends ServerSocket {

	public CustomServerSocket(int port) throws IOException {

		super(port);
	}

	@Override
	public Socket accept() throws IOException {

		Socket socket =  new CustomSocket();
		implAccept(socket);
		return socket;
	}

}
