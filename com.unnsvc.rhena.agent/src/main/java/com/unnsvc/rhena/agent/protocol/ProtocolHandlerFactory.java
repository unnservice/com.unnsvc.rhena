
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandlerFactory;

public class ProtocolHandlerFactory implements IProtocolHandlerFactory<IExecutionRequest, IExecutionResponse> {

	@Override
	public IProtocolHandler<IExecutionRequest, IExecutionResponse> newProtocolHandler() {

		return new ProtocolHandler();
	}
}
