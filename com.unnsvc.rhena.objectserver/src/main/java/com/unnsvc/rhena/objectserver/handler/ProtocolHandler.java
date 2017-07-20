
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.messages.PingRequest;
import com.unnsvc.rhena.objectserver.messages.PingResponse;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public class ProtocolHandler implements IProtocolHandler {

	public ProtocolHandler() {

	}

	@Override
	public IResponse handleRequest(IRequest request) {

		if (request instanceof PingRequest) {

			return new PingResponse(((PingRequest) request).getId());
		}

		throw new UnsupportedOperationException("Not implemented");
	}
}
