
package com.unnsvc.rhena.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class LifecycleReference implements ILifecycleReference {

	private String name;
	private IExecutionReference context;
	private List<IProcessorReference> processors;
	private IGeneratorReference generator;

	public LifecycleReference(String name) {

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

	public void setContext(IExecutionReference context) {

		this.context = context;
	}

	@Override
	public IExecutionReference getContext() {

		return context;
	}

	@Override
	public List<ILifecycleProcessorReference> getAllReferences() {

		List<ILifecycleProcessorReference> allrefs = new ArrayList<ILifecycleProcessorReference>();
		allrefs.add(getContext());
		getProcessors().forEach(proc -> allrefs.add(proc));
		allrefs.add(getGenerator());
		return allrefs;
	}
}
