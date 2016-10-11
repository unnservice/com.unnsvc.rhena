
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IGeneratorReference {

	public IRhenaModule getModule();

	public void setModule(IRhenaModule gModule);

	public String getClazz();

	public Node getConfiguration();

}
