
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.IContextInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class ContextProcessorInstance extends ProcessorInstance implements IContextInstance {

	public ContextProcessorInstance(IDependencies dependencies) {

		super(dependencies);
	}

	public ContextProcessorInstance() {

	}

}
