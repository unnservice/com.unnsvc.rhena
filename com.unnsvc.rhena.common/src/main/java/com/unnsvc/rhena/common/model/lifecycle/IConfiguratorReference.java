package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IConfiguratorReference {

	public IRhenaModule getModule();

	public String getClazz();

	public Node getConfiguration();

}
