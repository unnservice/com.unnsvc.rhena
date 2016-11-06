
package com.unnsvc.rhena.core.logging;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.common.logging.IRhenaLoggingHandler;

public class RhenaLogger implements IRhenaLoggingHandler {

	private IRhenaContext context;
	private String source;

	public RhenaLogger(IRhenaContext context, String source) {

		this.context = context;
		this.source = source;
	}

	@Override
	public void info(ModuleIdentifier identifier, EExecutionType type, String message) {

		fireEvent(ELogLevel.INFO, identifier, type, message);
	}

	@Override
	public void error(ModuleIdentifier identifier, EExecutionType type, String message) {

		fireEvent(ELogLevel.ERROR, identifier, type, message);
	}

	@Override
	public void debug(ModuleIdentifier identifier, EExecutionType type, String message) {

		fireEvent(ELogLevel.DEBUG, identifier, type, message);
	}

	@Override
	public void error(String message) {

		fireEvent(ELogLevel.ERROR, message);
	}

	@Override
	public void debug(String message) {

		fireEvent(ELogLevel.DEBUG, message);
	}

	@Override
	public void info(String message) {

		fireEvent(ELogLevel.INFO, message);
	}

	private void fireEvent(ELogLevel level, String message) {

		fireEvent(level, null, null, message);
	}

	private void fireEvent(ELogLevel level, ModuleIdentifier identifier, EExecutionType type, String message) {

		LogEvent evt = new LogEvent(source, level, identifier, type, message);
		try {
			context.fireEvent(evt);
		} catch (RhenaException e) {

			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
