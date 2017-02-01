
package com.unnsvc.rhena.agent.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CustomSocket extends Socket {
	
	private ByteArrayOutputStream baos;
	private ByteArrayInputStream bais;

	@Override
	public OutputStream getOutputStream() throws IOException {

		System.err.println("Socket output stream: " +  super.getOutputStream().getClass());
		return super.getOutputStream();
	}

	@Override
	public InputStream getInputStream() throws IOException {

		System.err.println("Socket input stream: " +  super.getInputStream().getClass());
		return super.getInputStream();
	}
}
