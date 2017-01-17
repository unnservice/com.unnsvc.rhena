
package com.unnsvc.rhena.core.lifecycle;

import java.net.URL;
import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.lifecycle.ICustomLifecycleProcessorExecutable;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class CustomProcessorExecutable extends ProcessorExecutable implements ICustomLifecycleProcessorExecutable {

	private static final long serialVersionUID = 1L;
	private String schema;
	private Document configuration;
	private IDependencies dependencies;

	public CustomProcessorExecutable(ILifecycleProcessorReference processor, IDependencies dependencies) {

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
	public IDependencies getDependencies() {

		return dependencies;
	}
}
