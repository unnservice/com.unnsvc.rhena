
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Node;

import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * Common interface for all processors in ILifecycleDeclaration
 * 
 * @author noname
 *
 */
public interface ILifecycleProcessorReference {

	public IRhenaModule getModule();

	public void setModule(IRhenaModule module);

	public String getClazz();

	public Node getConfiguration();
}
