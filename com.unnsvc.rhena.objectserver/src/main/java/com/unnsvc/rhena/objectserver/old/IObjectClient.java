
package com.unnsvc.rhena.objectserver.old;

public interface IObjectClient<REQUEST extends IObjectRequest, REPLY extends IObjectReply> extends AutoCloseable {

	public REPLY executeRequest(REQUEST request) throws ObjectServerException;

	public void close() throws ObjectServerException;

}
