
package com.unnsvc.rhena.objectserver.stream.misc;

import java.io.Serializable;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;

public class ObjectProtocolHandler implements IObjectProtocolHandler {

	@Override
	public IRequest handleRequest(Serializable read) throws Exception {

		throw new UnsupportedOperationException("Not implemented");
	}

}
