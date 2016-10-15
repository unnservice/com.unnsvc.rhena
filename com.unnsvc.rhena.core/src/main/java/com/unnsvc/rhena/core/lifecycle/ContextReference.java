
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class ContextReference implements IExecutionReference {

	private String clazz;
	private Document configuration;
	private String schema;
	private IRhenaEdge moduleEdge;

	public ContextReference(IRhenaModule module, String clazz, String schema, Document configuration) {

		this.moduleEdge = new RhenaEdge(EExecutionType.FRAMEWORK, module, TraverseType.SCOPE);
		this.clazz = clazz;
		this.schema = schema;
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
