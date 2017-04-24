
package com.unnsvc.rhena.lifecycle.instance;

import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class ProcessorInstance implements IProcessorInstance {

	private IDependencies dependencies;

	public ProcessorInstance(IDependencies dependencies) {

		this.dependencies = dependencies;
	}
	
	public ProcessorInstance() {
		
	}

	@Override
	public IDependencies getDependencies() {

		return dependencies;
	}

}
