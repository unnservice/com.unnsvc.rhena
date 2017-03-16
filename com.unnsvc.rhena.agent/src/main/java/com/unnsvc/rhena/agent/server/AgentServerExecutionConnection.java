
package com.unnsvc.rhena.agent.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.unnsvc.rhena.agent.LifecycleAgent;
import com.unnsvc.rhena.agent.client.ExecutionRequest;
import com.unnsvc.rhena.common.agent.ILifecycleAgent;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;

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
			ExecutionRequest request = (ExecutionRequest) read;
			System.out.println("server: Read object from client: " + request + " in server classloader " + Thread.currentThread().getContextClassLoader());

			ILifecycleAgent agent = new LifecycleAgent();
			ILifecycleExecutionResult result = agent.executeLifecycle(request);

			oos.writeObject(result);
			System.out.println("server: Wrote result to client");
		} catch (IOException | ClassNotFoundException ioe) {

			ioe.printStackTrace();

			// close connection
			// @TODO send exception back over the channel
			try {
				clientSocket.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}
