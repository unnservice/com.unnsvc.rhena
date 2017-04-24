
package com.unnsvc.rhena.model.lifecycle;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IProcessorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public abstract class AbstractProcessorSpec implements IProcessorSpec {

	private String schema;
	private String clazz;
	private Document config;

	private List<IRhenaEdge> processorDeps;

	public AbstractProcessorSpec(String schema, String clazz, Document config, List<IRhenaEdge> processorDeps) {

		this.schema = schema;
		this.clazz = clazz;
		this.config = config;
		this.processorDeps = processorDeps;
	}

	@Override
	public String getSchema() {

		return schema;
	}

	public void setSchema(String schema) {

		this.schema = schema;
	}

	@Override
	public String getClazz() {

		return clazz;
	}

	public void setClazz(String clazz) {

		this.clazz = clazz;
	}

	@Override
	public Document getConfig() {

		return config;
	}

	public void setConfig(Document config) {

		this.config = config;
	}

	public List<IRhenaEdge> getProcessorDeps() {

		return processorDeps;
	}

	public void setProcessorDeps(List<IRhenaEdge> processorDeps) {

		this.processorDeps = processorDeps;
	}

	@Override
	public Iterator<IRhenaEdge> iterator() {

		return processorDeps.iterator();
	}
}
