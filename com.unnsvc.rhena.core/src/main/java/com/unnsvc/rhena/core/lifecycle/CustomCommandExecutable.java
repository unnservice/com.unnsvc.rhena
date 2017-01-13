
package com.unnsvc.rhena.core.lifecycle;

import java.net.URL;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleCommandExecutable;

public class CustomCommandExecutable extends CustomProcessorExecutable implements ICustomLifecycleCommandExecutable {

	private static final long serialVersionUID = 1L;
	private String commandName;

	public CustomCommandExecutable(CommandProcessorReference command, List<URL> dependencies) {

		super(command, dependencies);
		this.commandName = command.getCommandName();
	}

	@Override
	public String getCommandName() {

		return commandName;
	}
}
