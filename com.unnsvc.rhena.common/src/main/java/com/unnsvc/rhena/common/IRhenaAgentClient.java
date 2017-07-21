
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IRhenaAgentClient {

	public IResponse submitRequest(IRequest request) throws ObjectServerException;
}
