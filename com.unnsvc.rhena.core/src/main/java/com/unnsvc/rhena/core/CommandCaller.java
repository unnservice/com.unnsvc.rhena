
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.ICommandCaller;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * Support multiple commands
 * @author noname
 *
 */
public class CommandCaller extends Caller implements ICommandCaller {

	private static final long serialVersionUID = 1L;
	private String command;

	public CommandCaller(IRhenaModule module, EExecutionType executionType, String command) {

		super(module, executionType);
		this.command = command;
	}

	@Override
	public String getCommand() {

		return command;
	}

	@Override
	public String toString() {

		return "CommandCaller [getEntryPoint()=" + getEntryPoint() + ", command=" + command + "]";
	}
}
