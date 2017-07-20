
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public class ProtocolHandler implements IProtocolHandler {

	@Override
	public IResponse handleRequest(IRequest request) throws ObjectServerException {

		throw new UnsupportedOperationException("Not implemented");
	}

}
