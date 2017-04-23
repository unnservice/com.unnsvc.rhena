package com.unnsvc.rhena.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class ProcessorReference extends AbstractLifecycleReference {

	private static final long serialVersionUID = 1L;

	public ProcessorReference(String schema, String clazz, Document config,  ModuleIdentifier source, ESelectionType selectionType, IEntryPoint entryPoint) {

		super(schema, clazz, config, source, selectionType, entryPoint);
	}

}
