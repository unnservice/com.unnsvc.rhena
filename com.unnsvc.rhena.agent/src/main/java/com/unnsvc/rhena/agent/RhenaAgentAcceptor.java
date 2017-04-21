
package com.unnsvc.rhena.agent;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;

public class RhenaAgentAcceptor implements IObjectServerAcceptor<IExecutionRequest, IExecutionResult> {

	@Override
	public IExecutionResult onRequest(IExecutionRequest request) {

		return null;
	}

	@Override
	public int getSocketReadTimeout() {

		return 0;
	}

}
