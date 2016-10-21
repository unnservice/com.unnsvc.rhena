package com.unnsvc.rhena.common.logging;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaLogger {
	
	/**
	 * Module specific logging
	 */
	

	public void info(ModuleIdentifier identifier, EExecutionType type, String message);
	
	public void error(ModuleIdentifier identifier, EExecutionType type, String message);

	public void debug(ModuleIdentifier identifier, EExecutionType type, String message);


	/**
	 * Generic loggers
	 */	
	public void error(String message);

	public void debug(String message);

	public void info(String message);

}
