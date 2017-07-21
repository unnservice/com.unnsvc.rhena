
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IRhenaAgentClient<REQUEST extends IRequest, RESPONSE extends IResponse> {

	public RESPONSE submitRequest(REQUEST request) throws ObjectServerException;
}
