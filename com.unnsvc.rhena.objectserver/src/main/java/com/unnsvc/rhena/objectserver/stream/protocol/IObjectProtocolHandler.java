
package com.unnsvc.rhena.objectserver.stream.protocol;

import java.io.Serializable;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;

public interface IObjectProtocolHandler {

	public IRequest handleRequest(Serializable read) throws Exception;

}
