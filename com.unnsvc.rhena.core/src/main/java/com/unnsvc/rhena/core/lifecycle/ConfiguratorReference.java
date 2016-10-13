
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionReference;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class ConfiguratorReference extends RhenaEdge implements IExecutionReference {

	private String clazz;
	private Document configuration;
	private String schema;

	public ConfiguratorReference(IRhenaModule module, String clazz, String schema, Document configuration, ExecutionType et, TraverseType tt) {

		super(et, module, tt);
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
}
