
package com.unnsvc.rhena.objectserver;

public interface IObjectClient<REQUEST extends IObjectRequest, REPLY extends IObjectReply> {

	public REPLY executeRequest(REQUEST request) throws ObjectServerException;

	public void close() throws ObjectServerException;

}
