
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * Common interface for all processors in ILifecycleDeclaration
 * 
 * @author noname
 *
 */
public interface ILifecycleProcessorReference extends IRhenaEdge {

	public IRhenaModule getTarget();

	public void setTarget(IRhenaModule module);

	public String getClazz();

	public Document getConfiguration();
}
