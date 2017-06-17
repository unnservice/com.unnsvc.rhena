
package com.unnsvc.rhena.objectserver.old;

/**
 * This is executed in a separate thread
 * @author noname
 *
 */
public interface IObjectServerAcceptor<REQUEST extends IObjectRequest, REPLY extends IObjectReply> {

	public REPLY onRequest(REQUEST request) throws Throwable;
}

