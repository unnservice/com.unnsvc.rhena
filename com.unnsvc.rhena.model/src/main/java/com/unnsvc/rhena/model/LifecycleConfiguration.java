
package com.unnsvc.rhena.model;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.ng.model.ILifecycleConfiguration;
import com.unnsvc.rhena.model.lifecycle.CommandReference;
import com.unnsvc.rhena.model.lifecycle.ContextReference;
import com.unnsvc.rhena.model.lifecycle.GeneratorReference;
import com.unnsvc.rhena.model.lifecycle.ProcessorReference;

public class LifecycleConfiguration implements ILifecycleConfiguration {

	private String name;
	private ContextReference contextReference;
	private List<ProcessorReference> processorReferences;
	private GeneratorReference generatorReference;
	private List<CommandReference> commandReferences;

	public LifecycleConfiguration(String name) {

		this.name = name;
		this.processorReferences = new ArrayList<ProcessorReference>();
		this.commandReferences = new ArrayList<CommandReference>();
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) {

		this.name = name;
	}

	public void setContext(ContextReference contextReference) {

		this.contextReference = contextReference;
	}

	public void addProcessor(ProcessorReference processorReference) {

		this.processorReferences.add(processorReference);
	}

	public void setGenerator(GeneratorReference generatorReference) {

		this.generatorReference = generatorReference;
	}

	public void addCommand(CommandReference commandReference) {

		this.commandReferences.add(commandReference);
	}

}
