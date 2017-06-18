
package com.unnsvc.rhena.objectserver.stream.protocol;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public interface IObjectProtocolHandler {

	public IResponse handleRequest(IRequest request) throws Exception;

}
