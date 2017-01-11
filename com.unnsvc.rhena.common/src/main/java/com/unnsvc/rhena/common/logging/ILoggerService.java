package com.unnsvc.rhena.common.logging;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface ILoggerService extends Serializable, Remote {

	public void info(Class<?> clazz, ModuleIdentifier identifier, String message)  throws RemoteException;

	public void info(Class<?> clazz, String message)  throws RemoteException;

	public void error(Class<?> clazz, ModuleIdentifier identifier, String message)  throws RemoteException;

	public void error(Class<?> clazz, String message)  throws RemoteException;

	public void debug(Class<?> clazz, String message)  throws RemoteException;

	public void debug(Class<?> clazz, ModuleIdentifier identifier, String message)  throws RemoteException;

	public void warn(Class<?> clazz, String message)  throws RemoteException;

	public void warn(Class<?> clazz, ModuleIdentifier identifier, String message)  throws RemoteException;

	public void trace(Class<?> clazz, String message)  throws RemoteException;

	public void trace(Class<?> clazz, ModuleIdentifier identifier, String message)  throws RemoteException;
	
	public void fireLogEvent(ELogLevel level, Class<?> clazz, ModuleIdentifier identifier, String message, Throwable throwable) throws RemoteException;
}
