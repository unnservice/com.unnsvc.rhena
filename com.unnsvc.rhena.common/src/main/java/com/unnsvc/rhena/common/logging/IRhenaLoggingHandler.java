package com.unnsvc.rhena.common.logging;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaLoggingHandler {
	
	/**
	 * Module specific logging
	 */
	

	public void info(ModuleIdentifier identifier, String message);
	
	public void warn(ModuleIdentifier identifier, String message);
	
	public void error(ModuleIdentifier identifier, String message);

	public void debug(ModuleIdentifier identifier, String message);


	/**
	 * Generic loggers
	 */	
	public void error(String message);

	public void debug(String message);

	public void info(String message);

}
