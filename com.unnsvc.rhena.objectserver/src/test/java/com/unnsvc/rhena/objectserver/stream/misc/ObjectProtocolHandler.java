
package com.unnsvc.rhena.objectserver.stream.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.ConnectionProtocolException;
import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;

public class ObjectProtocolHandler implements IObjectProtocolHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IResponse handleRequest(IRequest request) throws ConnectionProtocolException {

		log.info("Replying to request: " + request);
		return new SuccessfulResponse();
	}

}
