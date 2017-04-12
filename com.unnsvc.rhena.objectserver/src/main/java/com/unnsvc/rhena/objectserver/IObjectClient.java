
package com.unnsvc.rhena.objectserver;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IObjectClient {

	public IReply executeRequest(IRequest request) throws RhenaException;

}
