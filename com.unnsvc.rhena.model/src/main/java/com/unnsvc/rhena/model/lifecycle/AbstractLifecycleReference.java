
package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.model.ILifecycleReference;
import com.unnsvc.rhena.common.ng.model.IRhenaEdge;

public abstract class AbstractLifecycleReference implements ILifecycleReference {

	private String schema;
	private String clazz;
	private Document config;
	private IRhenaEdge edge;

	public AbstractLifecycleReference(String schema, String clazz, Document config, IRhenaEdge edge) {

		this.schema = schema;
		this.clazz = clazz;
		this.config = config;
		this.edge = edge;
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

	public Document getConfig() {

		return config;
	}

	public void setConfig(Document config) {

		this.config = config;
	}

	public IRhenaEdge getEdge() {

		return edge;
	}

	public void setEdge(IRhenaEdge edge) {

		this.edge = edge;
	}

}
