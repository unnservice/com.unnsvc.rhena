
package com.unnsvc.rhena.objectserver.stream.misc;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;

public class ObjectProtocolHandler implements IObjectProtocolHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IResponse handleRequest(Serializable read) throws Exception {

		log.info("Replying to request: " + read);
		return new SuccessfulResponse();
	}

}
