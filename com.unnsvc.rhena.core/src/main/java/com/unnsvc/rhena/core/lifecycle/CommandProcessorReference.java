
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaEdge;

public class CommandProcessorReference extends ProcessorReference {

	private static final long serialVersionUID = 1L;
	private String commandName;

	public CommandProcessorReference(String commandName, IRhenaEdge edge, String clazz, String schema, Document configuration) {

		super(edge, clazz, schema, configuration);

		this.commandName = commandName;
	}

	public String getCommandName() {

		return commandName;
	}
}
