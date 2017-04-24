
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class ProcessorInstance implements IProcessorInstance {

	private String className;
	private IDependencies dependencies;

	public ProcessorInstance(IDependencies dependencies, String className) {

		this.dependencies = dependencies;
		this.className = className;
	}

	public ProcessorInstance() {

	}

	@Override
	public IDependencies getDependencies() {

		return dependencies;
	}

	@Override
	public String getClassName() {

		return className;
	}

}
