
package com.unnsvc.rhena.lifecycle.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.lifecycle.ICommandInstance;
import com.unnsvc.rhena.common.lifecycle.IContextInstance;
import com.unnsvc.rhena.common.lifecycle.IGeneratorInstance;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.traversal.Dependencies;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class DefaultLifecycleInstance implements ILifecycleInstance {

	private static final long serialVersionUID = 1L;
	private String lifecycleName;
	private IContextInstance context;
	private List<IProcessorInstance> processors;
	private IGeneratorInstance generator;
	private List<ICommandInstance> commands;

	public DefaultLifecycleInstance(IContextInstance context, List<IProcessorInstance> processors, IGeneratorInstance generator,
			List<ICommandInstance> commands) {

		this.lifecycleName = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
		this.context = context;
		this.processors = processors;
		this.generator = generator;
		this.commands = commands;
	}

	@Override
	public Iterator<IProcessorInstance> iterator() {

		List<IProcessorInstance> procs = new ArrayList<IProcessorInstance>();
		procs.add(context);
		procs.addAll(processors);
		procs.add(generator);
		procs.addAll(commands);
		return procs.iterator();
	}

	@Override
	public List<ICommandInstance> getCommands() {

		return commands;
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}

	@Override
	public IDependencies getDependencies() {

		return new Dependencies(Collections.EMPTY_LIST);
	}

	@Override
	public IContextInstance getContext() {

		return context;
	}

	@Override
	public List<IProcessorInstance> getProcessors() {

		return processors;
	}

	@Override
	public IGeneratorInstance getGenerator() {

		return generator;
	}

}
