
package com.unnsvc.rhena.profiling;

import java.lang.instrument.Instrumentation;

public class ProfilingAgent {

	public static void premain(String args, Instrumentation instrumentation) {

		ClassLoaderReporting transformer = new ClassLoaderReporting(instrumentation);
		instrumentation.addTransformer(transformer);

		/**
		 * @TODO start rmi client and report back into rhena. Use
		 *       instrumentation.getAllLoadedClasses(). Use
		 *       instrumentation.appendToBootstrapClassLoaderSearch(jarfile) to
		 *       append the common interfaces
		 */
	}
}
