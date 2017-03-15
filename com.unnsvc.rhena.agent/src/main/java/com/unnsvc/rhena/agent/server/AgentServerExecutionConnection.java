
package com.unnsvc.rhena.agent.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.agent.lifecycle.LifecycleExecutionResult;

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
			System.out.println("server: Read object from client: " + read + " in server classloader " + Thread.currentThread().getContextClassLoader());
			oos.writeObject(new LifecycleExecutionResult(null, null));
			System.out.println("server: Wrote result to client");
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}
}
