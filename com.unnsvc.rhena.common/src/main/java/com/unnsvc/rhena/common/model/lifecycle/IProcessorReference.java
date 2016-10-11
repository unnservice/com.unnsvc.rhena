
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IProcessorReference {

	public IRhenaModule getModule();

	public void setModule(IRhenaModule module);

	public String getClazz();

	public Node getConfiguration();

}
