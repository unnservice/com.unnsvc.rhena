
package com.unnsvc.rhena.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;

public class LifecycleReference implements ILifecycleReference {

	private static final long serialVersionUID = 1L;
	private String name;
	private ILifecycleProcessorReference context;
	private List<ILifecycleProcessorReference> processors;
	private ILifecycleProcessorReference generator;

	public LifecycleReference(String name) {

		this.name = name;
		this.processors = new ArrayList<ILifecycleProcessorReference>();
	}

	@Override
	public String getName() {

		return name;
	}

	public void addProcessor(ILifecycleProcessorReference processor) {

		this.processors.add(processor);
	}

	@Override
	public List<ILifecycleProcessorReference> getProcessors() {

		return processors;
	}

	public void setGenerator(ILifecycleProcessorReference generator) {

		this.generator = generator;
	}

	@Override
	public ILifecycleProcessorReference getGenerator() {

		return generator;
	}

	public void setContext(ILifecycleProcessorReference context) {

		this.context = context;
	}

	@Override
	public ILifecycleProcessorReference getContext() {

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
