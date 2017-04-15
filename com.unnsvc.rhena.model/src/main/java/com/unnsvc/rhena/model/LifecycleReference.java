
package com.unnsvc.rhena.model;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.ILifecycleReference;

public class LifecycleReference extends RhenaEdge implements ILifecycleReference {

	private String schema;
	private String clazz;
	private Document configuration;

	public LifecycleReference(ModuleIdentifier source, ESelectionType traverseType, IEntryPoint entryPoint, String schema, String clazz,
			Document configuration) {

		super(source, traverseType, entryPoint);
		this.schema = schema;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	public String getSchema() {

		return schema;
	}

	public void setSchema(String schema) {

		this.schema = schema;
	}

	public String getClazz() {

		return clazz;
	}

	public void setClazz(String clazz) {

		this.clazz = clazz;
	}

	public Document getConfiguration() {

		return configuration;
	}

	public void setConfiguration(Document configuration) {

		this.configuration = configuration;
	}
}
