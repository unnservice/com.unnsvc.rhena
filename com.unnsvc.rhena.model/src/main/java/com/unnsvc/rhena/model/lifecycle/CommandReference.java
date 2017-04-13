
package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.model.IRhenaEdge;

public class CommandReference extends AbstractLifecycleReference {

	private String commandName;

	public CommandReference(String schema, String clazz, Document config, IRhenaEdge edge, String commandName) {

		super(schema, clazz, config, edge);
		this.commandName = commandName;
	}

	public String getCommandName() {

		return commandName;
	}

	public void setCommandName(String commandName) {

		this.commandName = commandName;
	}
}
