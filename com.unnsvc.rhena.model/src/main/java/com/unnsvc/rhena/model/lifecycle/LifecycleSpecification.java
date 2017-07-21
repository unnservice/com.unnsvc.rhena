
package com.unnsvc.rhena.model.lifecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.model.ICommandSpec;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IProcessorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class LifecycleSpecification implements ILifecycleSpecification {

	private static final long serialVersionUID = 1L;
	private String name;
	private List<IRhenaEdge> lifecycleDependencies;
	private ContextSpec contextReference;
	private List<IProcessorSpec> processorReferences;
	private GeneratorSpec generatorReference;
	private List<ICommandSpec> commandReferences;
	private boolean resolved;

	public LifecycleSpecification(String name) {

		this.name = name;
		this.lifecycleDependencies = new ArrayList<IRhenaEdge>();
		this.resolved = false;
		this.processorReferences = new ArrayList<IProcessorSpec>();
		this.commandReferences = new ArrayList<ICommandSpec>();
	}

	/**
	 * Default lifecycle configuration instance
	 */
	public LifecycleSpecification() {

		this.name = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
		this.resolved = true;
		this.lifecycleDependencies = new ArrayList<IRhenaEdge>();
		this.processorReferences = new ArrayList<IProcessorSpec>();
		this.commandReferences = new ArrayList<ICommandSpec>();
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

	@Override
	public ContextSpec getContextReference() {

		return contextReference;
	}

	public void setContext(ContextSpec contextReference) {

		this.contextReference = contextReference;
	}

	@Override
	public List<IProcessorSpec> getProcessorReferences() {

		return processorReferences;
	}

	public void addProcessor(ProcessorSpec processorReference) {

		this.processorReferences.add(processorReference);
	}

	@Override
	public GeneratorSpec getGeneratorReference() {

		return generatorReference;
	}

	public void setGenerator(GeneratorSpec generatorReference) {

		this.generatorReference = generatorReference;
	}

	@Override
	public List<ICommandSpec> getCommandReferences() {

		return commandReferences;
	}

	public void addCommand(CommandSpec commandReference) {

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
	public List<IProcessorSpec> processorIterator() {

		List<IProcessorSpec> refs = new ArrayList<IProcessorSpec>();
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
