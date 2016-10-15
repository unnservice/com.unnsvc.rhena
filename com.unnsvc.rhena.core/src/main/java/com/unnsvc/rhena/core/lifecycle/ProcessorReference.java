
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class ProcessorReference extends RhenaEdge implements IProcessorReference {

	private String clazz;
	private String schema;
	private Document configuration;

	public ProcessorReference(IRhenaModule module, String clazz, String schema, Document configuration, EExecutionType et, TraverseType tt) {

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
