
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IProtocolHandler<REQUEST extends IRequest, RESPONSE extends IResponse> {

	public RESPONSE handleRequest(REQUEST request) throws ObjectServerException;

}
