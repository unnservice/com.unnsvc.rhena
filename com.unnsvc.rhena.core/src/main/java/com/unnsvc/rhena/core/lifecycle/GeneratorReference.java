
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;

public class GeneratorReference implements IGeneratorReference {

	private static final long serialVersionUID = 1L;
	private String clazz;
	private String schema;
	private Document configuration;
	private IRhenaEdge moduleEdge;

	public GeneratorReference(IRhenaEdge edge, String clazz, String schema, Document configuration) {

		this.moduleEdge = edge;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	@Override
	public String getSchema() {

		return schema;
	}

	@Override
	public String getClazz() {

		return clazz;
	}

	@Override
	public Document getConfiguration() {

		return configuration;
	}

	@Override
	public IRhenaEdge getModuleEdge() {

		return moduleEdge;
	}
}
