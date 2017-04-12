
package com.unnsvc.rhena.objectserver;

/**
 * This is executed in a separate thread
 * @author noname
 *
 */
public interface IObjectServerAcceptor {

	public IReply onRequest(IRequest request);

	public int getSocketReadTimeout();
}
