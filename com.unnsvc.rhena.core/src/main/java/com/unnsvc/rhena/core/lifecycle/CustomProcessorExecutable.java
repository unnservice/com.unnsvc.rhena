
package com.unnsvc.rhena.core.lifecycle;

import java.net.URL;
import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;

public class CustomProcessorExecutable extends ProcessorExecutable implements ICustomLifecycleProcessorExecutable {

	private static final long serialVersionUID = 1L;
	private String schema;
	private Document configuration;
	private List<URL> dependencies;

	public CustomProcessorExecutable(ILifecycleProcessorReference processor, List<URL> dependencies) {

		super(processor.getClazz());
		this.schema = processor.getSchema();
		this.configuration = processor.getConfiguration();
		this.dependencies = dependencies;
	}

	@Override
	public String getSchema() {

		return schema;
	}

	@Override
	public Document getConfiguration() {

		return configuration;
	}

	@Override
	public List<URL> getDependencies() {

		return dependencies;
	}
}
