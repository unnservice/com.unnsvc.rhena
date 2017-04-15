package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;

public class ContextReference extends AbstractLifecycleReference {

	public ContextReference(String schema, String clazz, Document config, ModuleIdentifier source, ESelectionType selectionType, IEntryPoint entryPoint) {

		super(schema, clazz, config, source, selectionType, entryPoint);
	}

}
