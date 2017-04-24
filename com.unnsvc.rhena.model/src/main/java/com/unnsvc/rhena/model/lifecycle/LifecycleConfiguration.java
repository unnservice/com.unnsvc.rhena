
package com.unnsvc.rhena.model.lifecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.model.ILifecycleReference;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class LifecycleConfiguration implements ILifecycleConfiguration {

	private static final long serialVersionUID = 1L;
	private String name;
	private List<IRhenaEdge> lifecycleDependencies;
	private ContextReference contextReference;
	private List<ProcessorReference> processorReferences;
	private GeneratorReference generatorReference;
	private List<CommandReference> commandReferences;
	private boolean resolved;

	public LifecycleConfiguration(String name) {

		this.name = name;
		this.lifecycleDependencies = new ArrayList<IRhenaEdge>();
		this.resolved = false;
		this.processorReferences = new ArrayList<ProcessorReference>();
		this.commandReferences = new ArrayList<CommandReference>();
	}

	/**
	 * Default lifecycle configuration instance
	 */
	public LifecycleConfiguration() {

		this.name = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
		this.resolved = true;
		this.processorReferences = new ArrayList<ProcessorReference>();
		this.commandReferences = new ArrayList<CommandReference>();
	}

	@Override
	public boolean isResolved() {

		return resolved;
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

	public void addLifecycleDependency(IRhenaEdge lifecycleDependency) {

		this.lifecycleDependencies.add(lifecycleDependency);
	}

	@Override
	public List<IRhenaEdge> getLifecycleDependencies() {

		return lifecycleDependencies;
	}

	@Override
	public List<ILifecycleReference> processorIterator() {

		List<ILifecycleReference> refs = new ArrayList<ILifecycleReference>();
		refs.add(contextReference);
		refs.addAll(processorReferences);
		refs.add(generatorReference);
		refs.addAll(commandReferences);
		return refs;
	}

	@Override
	public Iterator<IRhenaEdge> iterator() {

		return lifecycleDependencies.iterator();
	}

}
