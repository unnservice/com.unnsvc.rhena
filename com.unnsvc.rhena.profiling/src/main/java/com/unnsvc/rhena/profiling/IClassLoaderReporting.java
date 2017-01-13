
package com.unnsvc.rhena.profiling;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.profiling.report.IDiagnosticReport;

public interface IClassLoaderReporting extends Remote {

	public IDiagnosticReport produceRuntimeReport() throws RemoteException;

}
