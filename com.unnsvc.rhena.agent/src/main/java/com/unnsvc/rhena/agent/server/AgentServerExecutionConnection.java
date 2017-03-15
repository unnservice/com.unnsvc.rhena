
package com.unnsvc.rhena.agent.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AgentServerExecutionConnection extends Thread {

	private Socket clientSocket;

	public AgentServerExecutionConnection(Socket clientSocket) {

		this.clientSocket = clientSocket;
	}

	public void run() {

		try {
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			Object read = ois.readObject();
			System.err.println("Read object from client: " + read);
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}
}
