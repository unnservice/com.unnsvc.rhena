
package com.unnsvc.rhena.lifecycle.execution;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecution;

public class DefaultLifecycleExecution implements ILifecycleExecution {

	private static final long serialVersionUID = 1L;
	private String lifecycleName;

	public DefaultLifecycleExecution() {

		this.lifecycleName = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
	}
	
	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}
}
