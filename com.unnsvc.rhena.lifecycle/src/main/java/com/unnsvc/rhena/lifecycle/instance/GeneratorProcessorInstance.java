
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.IGeneratorInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class GeneratorProcessorInstance extends ProcessorInstance implements IGeneratorInstance {

	private static final long serialVersionUID = 1L;

	public GeneratorProcessorInstance(IDependencies dependencie, String className) {

		super(dependencie, className);
	}

	public GeneratorProcessorInstance() {

	}

}
