
package com.unnsvc.rhena.lifecycle.execution;

import com.unnsvc.rhena.common.lifecycle.ILifecycleExecution;
import com.unnsvc.rhena.common.traversal.IDependencies;

/**
 * An instance of a lifecycle reference
 * 
 * @author noname
 *
 */
public class LifecycleExecution implements ILifecycleExecution {

	private static final long serialVersionUID = 1L;

	private String lifecycleName;

	public LifecycleExecution(String lifecycleName, IDependencies dependencies) {

		this.lifecycleName = lifecycleName;
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}
}
