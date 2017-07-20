package com.unnsvc.rhena.objectserver.client;

import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IObjectClient {

	public IResponse submitRequest(IRequest request) throws ObjectServerException;

}
