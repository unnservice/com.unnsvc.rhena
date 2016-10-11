
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IGeneratorReference;

public class GeneratorReference implements IGeneratorReference {

	private IRhenaModule module;
	private String clazz;
	private Node configuration;

	public GeneratorReference(IRhenaModule module, String clazz, Node configuration) {

		this.module = module;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}

	@Override
	public void setModule(IRhenaModule module) {

		this.module = module;
	}

	@Override
	public String getClazz() {

		return clazz;
	}

	@Override
	public Node getConfiguration() {

		return configuration;
	}

}
