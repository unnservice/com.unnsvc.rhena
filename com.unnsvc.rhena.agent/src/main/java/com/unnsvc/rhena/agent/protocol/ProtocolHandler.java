
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;

public class ProtocolHandler implements IProtocolHandler<IExecutionRequest, IExecutionResponse> {

	@Override
	public IExecutionResponse handleRequest(IExecutionRequest request) throws ObjectServerException {

		throw new UnsupportedOperationException("Not implemented");
	}
}
