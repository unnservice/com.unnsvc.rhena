
package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class CommandReference extends AbstractLifecycleReference {

	private static final long serialVersionUID = 1L;
	private String commandName;

	public CommandReference(String schema, String clazz, Document config, String commandName, ModuleIdentifier source, ESelectionType selectionType, IEntryPoint entryPoint) {

		super(schema, clazz, config, source, selectionType, entryPoint);
		this.commandName = commandName;
	}

	public String getCommandName() {

		return commandName;
	}

	public void setCommandName(String commandName) {

		this.commandName = commandName;
	}

}
