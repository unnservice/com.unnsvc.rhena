
package com.unnsvc.rhena.lifecycle.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.ICommandInstance;
import com.unnsvc.rhena.common.lifecycle.IContextInstance;
import com.unnsvc.rhena.common.lifecycle.IGeneratorInstance;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

/**
 * An instance of a lifecycle reference
 * 
 * @author noname
 *
 */
public class LifecycleInstance implements ILifecycleInstance {

	private static final long serialVersionUID = 1L;

	private String lifecycleName;
	private IDependencies dependencies;

	private IContextInstance context;
	private List<IProcessorInstance> processors;
	private IGeneratorInstance generator;
	private List<ICommandInstance> commands;

	public LifecycleInstance(String lifecycleName, IDependencies dependencies, IContextInstance context, List<IProcessorInstance> processors,
			IGeneratorInstance generator, List<ICommandInstance> commands) {

		this.lifecycleName = lifecycleName;
		this.dependencies = dependencies;
		this.context = context;
		this.processors = processors;
		this.generator = generator;
		this.commands = commands;
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}

	@Override
	public IDependencies getDependencies() {

		return dependencies;
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

	@Override
	public List<ICommandInstance> getCommands() {

		return commands;
	}

	@Override
	public Iterator<IProcessorInstance> iterator() {

		List<IProcessorInstance> procs = new ArrayList<IProcessorInstance>();
		procs.add(getContext());
		if (!getProcessors().isEmpty()) {
			procs.addAll(getProcessors());
		}
		procs.add(getGenerator());
		if (!getCommands().isEmpty()) {
			procs.addAll(getCommands());
		}
		return procs.iterator();
	}

}
