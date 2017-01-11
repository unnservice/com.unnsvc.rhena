
package com.unnsvc.rhena.common.lifecycle;

import java.net.URL;
import java.util.List;

import org.w3c.dom.Document;

public interface ICustomLifecycleProcessorExecutable extends ILifecycleProcessorExecutable {

	public String getSchema();

	public Document getConfiguration();

	public List<URL> getDependencies();
}
