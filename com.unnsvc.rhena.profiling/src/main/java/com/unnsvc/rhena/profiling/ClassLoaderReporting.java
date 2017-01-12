
package com.unnsvc.rhena.profiling;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ClassLoaderReporting implements ClassFileTransformer {

	public ClassLoaderReporting(Instrumentation instrumentation) {

	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
			throws IllegalClassFormatException {

		System.err.println("Loading: " + className + " with loader: " + loader + " in logger " + this.toString());

		return classfileBuffer;
	}

}
