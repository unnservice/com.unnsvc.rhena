
package com.unnsvc.rhena.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class LifecycleDeclaration implements ILifecycleDeclaration {

	private String name;
	private IExecutionReference configurator;
	private List<IProcessorReference> processors;
	private IGeneratorReference generator;

	public LifecycleDeclaration(String name) {

		this.name = name;
		this.processors = new ArrayList<IProcessorReference>();
	}

	@Override
	public String getName() {

		return name;
	}

	public void addProcessor(IProcessorReference processor) {

		this.processors.add(processor);
	}

	@Override
	public List<IProcessorReference> getProcessors() {

		return processors;
	}

	public void setGenerator(IGeneratorReference generator) {

		this.generator = generator;
	}

	@Override
	public IGeneratorReference getGenerator() {

		return generator;
	}

	public void setConfigurator(IExecutionReference configurator) {

		this.configurator = configurator;
	}

	@Override
	public IExecutionReference getConfigurator() {

		return configurator;
	}
}
