
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IProtocolHandlerFactory<REQUEST extends IRequest, RESPONSE extends IResponse> {

	public IProtocolHandler<REQUEST, RESPONSE> newProtocolHandler();

}
