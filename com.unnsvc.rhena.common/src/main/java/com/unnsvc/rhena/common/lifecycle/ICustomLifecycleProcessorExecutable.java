
package com.unnsvc.rhena.common.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ICustomLifecycleProcessorExecutable extends ILifecycleProcessorExecutable {

	public String getSchema();

	public Document getConfiguration();

	public IDependencies getDependencies();
}
