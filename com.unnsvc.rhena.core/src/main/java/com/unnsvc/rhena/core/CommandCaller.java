
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.ICommandCaller;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class CommandCaller extends Caller implements ICommandCaller {

	private static final long serialVersionUID = 1L;
	private String command;

	public CommandCaller(ModuleIdentifier identifier, EExecutionType executionType, String command) {

		super(identifier, executionType);
		this.command = command;
	}

	@Override
	public String getCommand() {

		return command;
	}
}
