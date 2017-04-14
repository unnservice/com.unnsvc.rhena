
package com.unnsvc.rhena.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleCommandExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorExecutable;

public class LifecycleExecutable implements ILifecycleExecutable {

	private static final long serialVersionUID = 1L;
	private String lifecycleName;
	private ILifecycleProcessorExecutable contextExecutable;
	private List<ILifecycleProcessorExecutable> processorExecutables;
	private ILifecycleProcessorExecutable generatorExecutable;
	private List<ICustomLifecycleCommandExecutable> commandExecutables;

	public LifecycleExecutable(String lifecycleName) {

		this.lifecycleName = lifecycleName;
		this.processorExecutables = new ArrayList<ILifecycleProcessorExecutable>();
		this.commandExecutables = new ArrayList<ICustomLifecycleCommandExecutable>();
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}

	public void setContextExecutable(ProcessorExecutable contextExecutable) {

		this.contextExecutable = contextExecutable;
	}

	@Override
	public ILifecycleProcessorExecutable getContextExecutable() {

		return contextExecutable;
	}

	public void addProcessorExecutable(ILifecycleProcessorExecutable processorExecutable) {

		this.processorExecutables.add(processorExecutable);
	}

	@Override
	public List<ILifecycleProcessorExecutable> getProcessorExecutables() {

		return processorExecutables;
	}

	public void setGenerator(ILifecycleProcessorExecutable generatorExecutable) {

		this.generatorExecutable = generatorExecutable;
	}

	@Override
	public ILifecycleProcessorExecutable getGeneratorExecutable() {

		return generatorExecutable;
	}

	public void addCommandExecutable(ICustomLifecycleCommandExecutable commandExecutable) {

		this.commandExecutables.add(commandExecutable);
	}
	
	@Override
	public List<ICustomLifecycleCommandExecutable> getCommandExecutables() {

		return commandExecutables;
	}
}
