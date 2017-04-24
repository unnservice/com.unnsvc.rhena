
package com.unnsvc.rhena.model.lifecycle;

import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ICommandSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class CommandSpec extends AbstractProcessorSpec implements ICommandSpec {

	private String commandName;

	public CommandSpec(String schema, String clazz, Document config, String commandName, List<IRhenaEdge> processorDeps) {

		super(schema, clazz, config, processorDeps);
		this.commandName = commandName;
	}

	public String getCommandName() {

		return commandName;
	}

	public void setCommandName(String commandName) {

		this.commandName = commandName;
	}

}
