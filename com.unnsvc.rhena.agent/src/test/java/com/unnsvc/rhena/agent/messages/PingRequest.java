
package com.unnsvc.rhena.agent.messages;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.IDependencies;

public class PingRequest implements IExecutionRequest {

	private static final long serialVersionUID = 1L;

	@Override
	public IEntryPoint getEntryPoint() {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public IRhenaModule getModule() {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public ILifecycleInstance getLifecycle() {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public IDependencies getDependencies() {

		throw new UnsupportedOperationException("not implemented");
	}

}
