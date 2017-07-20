
package com.unnsvc.rhena.objectserver.ng.handler;

import com.unnsvc.rhena.objectserver.ng.ObjectServerException;
import com.unnsvc.rhena.objectserver.ng.messages.Request;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

public interface IProtocolHandler {

	public Response handleRequest(Request request) throws ObjectServerException;

}
