
package com.unnsvc.rhena.common.logging;

import java.rmi.RemoteException;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface ILogger {

	public void info(Class<?> clazz, ModuleIdentifier identifier, String message);

	public void info(Class<?> clazz, String message);

	public void error(Class<?> clazz, ModuleIdentifier identifier, String message);

	public void error(Class<?> clazz, String message);

	public void debug(Class<?> clazz, String message);

	public void debug(Class<?> clazz, ModuleIdentifier identifier, String message);

	public void warn(Class<?> clazz, String message);

	public void warn(Class<?> clazz, ModuleIdentifier identifier, String message);

	public void trace(Class<?> clazz, String message);

	public void trace(Class<?> clazz, ModuleIdentifier identifier, String message);

	public void fireLogEvent(ELogLevel level, Class<?> clazz, ModuleIdentifier identifier, String message, Throwable throwable) throws RhenaException;

	public void fireLogEvent(ELogLevel level, String loggerName, ModuleIdentifier identifier, String message, RemoteException exception) throws RhenaException;

}
