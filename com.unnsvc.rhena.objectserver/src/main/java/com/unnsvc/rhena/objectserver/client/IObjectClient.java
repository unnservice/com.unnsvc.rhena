
package com.unnsvc.rhena.objectserver.client;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IObjectClient<REQUEST extends IRequest, RESPONSE extends IResponse> {

	public RESPONSE submitRequest(REQUEST request) throws ObjectServerException;

}
