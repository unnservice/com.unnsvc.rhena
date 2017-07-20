
package com.unnsvc.rhena.objectserver.ng.handler;

import com.unnsvc.rhena.objectserver.ng.messages.PingRequest;
import com.unnsvc.rhena.objectserver.ng.messages.PingResponse;
import com.unnsvc.rhena.objectserver.ng.messages.Request;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

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
