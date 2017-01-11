
package com.unnsvc.rhena.agent.rmiexample;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class RmiServer {

	private Registry registry;
	
	public RmiServer() {
		
	}

	public static void main(String... args) throws Exception {

		RmiServer server = new RmiServer();
		server.start();
	}

	public int start() throws IOException {

		int port = findAvailablePort();
		System.err.println("Started on " + port);

		Registry registry = LocateRegistry.createRegistry(port);
		System.err.println("Listing registry " + Arrays.toString(registry.list()));

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {

				try {
					UnicastRemoteObject.unexportObject(registry, true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}));
		
		return port;
	}

	private int findAvailablePort() throws IOException {

		ServerSocket ss = new ServerSocket(0);
		ss.close();
		return ss.getLocalPort();
	}

	public Registry getRegistry() {

		return registry;
	}
}
