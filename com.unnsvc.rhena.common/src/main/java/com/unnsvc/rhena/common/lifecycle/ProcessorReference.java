
package com.unnsvc.rhena.common.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.RhenaModule;

public class ProcessorReference {

	private RhenaModule module;
	private String clazz;
	private Node configuration;

	public ProcessorReference(RhenaModule module, String clazz, Node configuration) {

		this.module = module;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	public RhenaModule getModule() {

		return module;
	}

	public void setModule(RhenaModule module) {

		this.module = module;
	}

	public String getClazz() {

		return clazz;
	}

	public Node getConfiguration() {

		return configuration;
	}
}
