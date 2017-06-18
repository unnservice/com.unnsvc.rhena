
package com.unnsvc.rhena.objectserver.stream;

import java.net.SocketAddress;

import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public interface ISocketClient {

	public void connect(SocketAddress endpoint) throws ConnectionException;

	public void stop() throws ConnectionException;

	public IResponse sendRequest(IRequest request, ERequestChannel channel) throws ConnectionException;

}
