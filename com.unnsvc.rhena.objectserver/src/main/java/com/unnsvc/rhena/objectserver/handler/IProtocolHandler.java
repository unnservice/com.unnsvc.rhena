
package com.unnsvc.rhena.objectserver.handler;

import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IProtocolHandler {

	public IResponse handleRequest(IRequest request);

}
