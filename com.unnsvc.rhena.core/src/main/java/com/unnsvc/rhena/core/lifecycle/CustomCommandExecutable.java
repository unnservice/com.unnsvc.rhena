
package com.unnsvc.rhena.core.lifecycle;

import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleCommandExecutable;
import com.unnsvc.rhena.common.search.IDependencies;

public class CustomCommandExecutable extends CustomProcessorExecutable implements ICustomLifecycleCommandExecutable {

	private static final long serialVersionUID = 1L;
	private String commandName;

	public CustomCommandExecutable(CommandProcessorReference command, IDependencies dependencies) {

		super(command, dependencies);
		this.commandName = command.getCommandName();
	}

	@Override
	public String getCommandName() {

		return commandName;
	}
}
