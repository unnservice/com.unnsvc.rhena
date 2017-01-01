
package com.unnsvc.rhena.common.logging;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface ILogger {

	public void info(Class<?> clazz, ModuleIdentifier identifier, String message) throws RhenaException;
	
	public void info(Class<?> clazz, String message) throws RhenaException;

	public void error(Class<?> clazz, ModuleIdentifier identifier, String message) throws RhenaException;
	
	public void error(Class<?> clazz, String message) throws RhenaException;

	public void debug(Class<?> clazz, String message) throws RhenaException;

	public void debug(Class<?> clazz, ModuleIdentifier identifier, String message) throws RhenaException;
	
	public void warn(Class<?> clazz, String message) throws RhenaException;

	public void warn(Class<?> clazz, ModuleIdentifier identifier, String message) throws RhenaException;
	
	public void trace(Class<?> clazz, String message) throws RhenaException;

	public void trace(Class<?> clazz, ModuleIdentifier identifier, String message) throws RhenaException;

}
