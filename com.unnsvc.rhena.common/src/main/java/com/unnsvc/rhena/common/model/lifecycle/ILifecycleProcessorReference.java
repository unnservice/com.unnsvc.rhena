
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.Serializable;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaEdge;

/**
 * Common interface for all processors in ILifecycleDeclaration
 * 
 * @author noname
 *
 */
public interface ILifecycleProcessorReference  extends Serializable {

	public String getSchema();

	public String getClazz();

	public Document getConfiguration();

	public IRhenaEdge getModuleEdge();
}
