package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.model.IRhenaEdge;

public class ContextReference extends AbstractLifecycleReference {

	public ContextReference(String schema, String clazz, Document config, IRhenaEdge edge) {

		super(schema, clazz, config, edge);
	}

}
