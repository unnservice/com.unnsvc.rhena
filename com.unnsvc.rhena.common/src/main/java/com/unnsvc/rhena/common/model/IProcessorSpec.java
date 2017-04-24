
package com.unnsvc.rhena.common.model;

import org.w3c.dom.Document;

public interface IProcessorSpec extends Iterable<IRhenaEdge> {

	public String getSchema();

	public String getClazz();

	public Document getConfig();

}
