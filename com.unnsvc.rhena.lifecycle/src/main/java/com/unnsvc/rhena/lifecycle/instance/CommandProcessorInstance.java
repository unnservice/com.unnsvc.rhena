
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.ICommandInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class CommandProcessorInstance extends ProcessorInstance implements ICommandInstance {

	private static final long serialVersionUID = 1L;

	public CommandProcessorInstance(IDependencies dependencies, String className) {

		super(dependencies, className);
	}

}
