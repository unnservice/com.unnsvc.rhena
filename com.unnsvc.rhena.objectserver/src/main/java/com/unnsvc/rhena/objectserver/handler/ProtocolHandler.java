
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.messages.PingRequest;
import com.unnsvc.rhena.objectserver.messages.PingResponse;
import com.unnsvc.rhena.objectserver.messages.Request;
import com.unnsvc.rhena.objectserver.messages.Response;

public class ProtocolHandler implements IProtocolHandler {

	public ProtocolHandler() {

	}

	@Override
	public Response handleRequest(Request request) {

		if (request instanceof PingRequest) {

			return new PingResponse(((PingRequest) request).getId());
		}

		throw new UnsupportedOperationException("Not implemented");
	}
}
