
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.IContextInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class ContextProcessorInstance extends ProcessorInstance implements IContextInstance {

	private static final long serialVersionUID = 1L;

	public ContextProcessorInstance(IDependencies dependencies, String className) {

		super(dependencies, className);
	}

	public ContextProcessorInstance() {

	}

}
