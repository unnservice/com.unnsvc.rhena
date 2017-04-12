
package com.unnsvc.rhena.objectserver;

/**
 * This is executed in a separate thread
 * @author noname
 *
 */
public interface IObjectServerAcceptor {

	public IObjectReply onRequest(IObjectRequest request);

	public int getSocketReadTimeout();
}
