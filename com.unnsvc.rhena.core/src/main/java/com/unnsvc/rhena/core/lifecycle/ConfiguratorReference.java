
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IConfiguratorReference;
import com.unnsvc.rhena.core.model.RhenaReference;

public class ConfiguratorReference implements IConfiguratorReference {

	private RhenaReference module;
	private String clazz;
	private Node configuration;

	public ConfiguratorReference(RhenaReference module, String clazz, Node configuration) {

		this.module = module;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
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
