
package com.unnsvc.rhena.common.lifecycle;

import com.unnsvc.rhena.common.model.ModuleIdentifier;

public class ProcessorReference {

	private ModuleIdentifier moduleIdentifier;
	private String clazz;

	public ProcessorReference(ModuleIdentifier moduleIdentifier, String clazz) {

		this.moduleIdentifier = moduleIdentifier;
		this.clazz = clazz;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public String getClazz() {

		return clazz;
	}
}
