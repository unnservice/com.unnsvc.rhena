
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.client.IObjectClient;
import com.unnsvc.rhena.objectserver.client.ObjectClient;
import com.unnsvc.rhena.objectserver.messages.ExceptionResponse;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public class RhenaAgentClient implements IRhenaAgentClient {

	private IObjectClient objectClient;

	public RhenaAgentClient(SocketAddress endpoint) {

		objectClient = new ObjectClient(endpoint);
	}

	@Override
	public IResponse submitRequest(IRequest request) throws RhenaException {

		try {
			IResponse response = objectClient.submitRequest(request);

			if (response instanceof ExceptionResponse) {

				/**
				 * Exception response gets rethrown in the client
				 */
				ExceptionResponse exceptionResponse = (ExceptionResponse) response;
				throw new ObjectServerException(exceptionResponse.getThrowable());
			}

			return response;
		} catch (ObjectServerException ose) {
			throw new RhenaException(ose);
		}
	}

}
