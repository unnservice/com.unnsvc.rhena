
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class GeneratorReference implements IGeneratorReference {

	private String clazz;
	private String schema;
	private Document configuration;
	private IRhenaEdge moduleEdge;

	public GeneratorReference(IRhenaModule module, String clazz, String schema, Document configuration) {

		this.moduleEdge = new RhenaEdge(EExecutionType.FRAMEWORK, module, TraverseType.SCOPE);
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
