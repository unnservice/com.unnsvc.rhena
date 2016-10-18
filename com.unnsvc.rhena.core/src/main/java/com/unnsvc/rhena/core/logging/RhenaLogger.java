
package com.unnsvc.rhena.core.logging;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.IRhenaLogger;

public class RhenaLogger implements IRhenaLogger {

	private IRhenaContext context;
	private String name;

	public RhenaLogger(IRhenaContext context, String name) {

		this.context = context;
		this.name = name;
	}

	@Override
	public void info(ModuleIdentifier identifier, EExecutionType type, String message) {

		System.out.println(name + " - " + message);
	}

	@Override
	public void error(ModuleIdentifier identifier, EExecutionType type, String message) {

		System.err.println(name + " - " + message);
	}

	@Override
	public void debug(ModuleIdentifier identifier, EExecutionType type, String message) {

		System.out.println(name + " - " + message);
	}

	@Override
	public void error(String message) {

		System.err.println(name + " - " + message);
	}

	@Override
	public void debug(String message) {

		System.out.println(name + " - " + message);
	}

	@Override
	public void info(String message) {

		System.out.println(name + " - " + message);
	}

}
