
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaEdge;

/**
 * Common interface for all processors in ILifecycleDeclaration
 * 
 * @author noname
 *
 */
public interface ILifecycleProcessorReference {

	public String getSchema();

	public String getClazz();

	public Document getConfiguration();

	public IRhenaEdge getModuleEdge();
}
