
package com.unnsvc.rhena.profiling;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.ProtectionDomain;

import com.unnsvc.rhena.profiling.report.DiagnosticReport;
import com.unnsvc.rhena.profiling.report.IDiagnosticReport;

public class ClassLoaderReporting extends UnicastRemoteObject implements ClassFileTransformer, IClassLoaderReporting {

	private static final long serialVersionUID = 1L;
	private Instrumentation instrumentation;

	public ClassLoaderReporting(Instrumentation instrumentation) throws RemoteException {

		super();
		this.instrumentation = instrumentation;
	}

	@Override 
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
			throws IllegalClassFormatException {

//		System.err.println("Loading: " + className + " with loader: " + loader + " in logger " + this.toString());
 
		return classfileBuffer;
	}

	@Override
	public IDiagnosticReport produceRuntimeReport() {

		return new DiagnosticReport(instrumentation.getAllLoadedClasses().length);
	}
}
