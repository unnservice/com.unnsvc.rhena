
package com.unnsvc.rhena.objectserver;

public interface IObjectClient {

	public IObjectReply executeRequest(IObjectRequest request) throws ObjectServerException;

	public void close() throws ObjectServerException;

}
