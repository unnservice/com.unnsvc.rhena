
package com.unnsvc.rhena.profiling;

import java.lang.instrument.Instrumentation;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ProfilingAgent {

	public static void premain(String args, Instrumentation instrumentation) throws Exception {

		args = args.substring(1, args.length() - 1);
		
		System.err.println("Running agent with arguments: " + args);

		int rmiRegistryPort = Integer.valueOf(args);
		final Registry registry = LocateRegistry.getRegistry(rmiRegistryPort);

		ClassLoaderReporting transformer = new ClassLoaderReporting(instrumentation);
		instrumentation.addTransformer(transformer);
		registry.bind(IClassLoaderReporting.class.getName(), transformer);
		System.err.println("Binded javaagent to rmi");

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {

				try {
					UnicastRemoteObject.unexportObject(transformer, true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}));

		/**
		 * @TODO start rmi client and report back into rhena. Use
		 *       instrumentation.getAllLoadedClasses(). Use
		 *       instrumentation.appendToBootstrapClassLoaderSearch(jarfile) to
		 *       append the common interfaces
		 */
	}
}
