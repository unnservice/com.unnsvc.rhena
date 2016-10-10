
package com.unnsvc.rhena.common.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class LifecycleDeclaration {

	private String name;
	private List<ProcessorReference> processors;
	private GeneratorReference generator;

	public LifecycleDeclaration(String name) {

		this.name = name;
		this.processors = new ArrayList<ProcessorReference>();
	}

	public String getName() {

		return name;
	}

	public void addProcessor(ProcessorReference processor) {

		this.processors.add(processor);
	}

	public List<ProcessorReference> getProcessors() {

		return processors;
	}

	public void setGenerator(GeneratorReference generator) {

		this.generator = generator;
	}

	public GeneratorReference getGenerator() {

		return generator;
	}
}
