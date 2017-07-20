
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.Request;
import com.unnsvc.rhena.objectserver.messages.Response;

public interface IProtocolHandler {

	public Response handleRequest(Request request) throws ObjectServerException;

}
