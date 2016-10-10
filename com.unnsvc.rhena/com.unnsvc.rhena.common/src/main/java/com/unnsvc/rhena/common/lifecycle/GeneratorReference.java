
package com.unnsvc.rhena.common.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.ModuleIdentifier;

public class GeneratorReference {

	private ModuleIdentifier moduleIdentifier;
	private String clazz;
	private Node configuration;

	public GeneratorReference(ModuleIdentifier moduleIdentifier, String clazz, Node configuration) {

		this.moduleIdentifier = moduleIdentifier;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public String getClazz() {

		return clazz;
	}

	public Node getConfiguration() {

		return configuration;
	}

}
