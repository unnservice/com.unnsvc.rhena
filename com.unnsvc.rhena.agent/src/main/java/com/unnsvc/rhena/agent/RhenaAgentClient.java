
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.client.IObjectClient;
import com.unnsvc.rhena.objectserver.client.ObjectClient;
import com.unnsvc.rhena.objectserver.messages.ExceptionResponse;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public class RhenaAgentClient implements IRhenaAgentClient<IExecutionRequest, IExecutionResponse> {

	private IObjectClient<IExecutionRequest, IExecutionResponse> objectClient;

	public RhenaAgentClient(SocketAddress endpoint) {

		objectClient = new ObjectClient<IExecutionRequest, IExecutionResponse>(endpoint);
	}

	@Override
	public IExecutionResponse submitRequest(IExecutionRequest request) throws ObjectServerException {

		IResponse response = objectClient.submitRequest(request);
		if (response instanceof ExceptionResponse) {

			/**
			 * Exception response gets rethrown in the client
			 */
			ExceptionResponse exceptionResponse = (ExceptionResponse) response;
			throw new ObjectServerException(exceptionResponse.getThrowable());
		} else if (response instanceof IExecutionResponse) {

			/**
			 * Normal response
			 */
			return (IExecutionResponse) response;
		} else {

			/**
			 * Unknown response type
			 */
			throw new ObjectServerException("Unknown response type: " + response.getClass().getName());
		}
	}

}
