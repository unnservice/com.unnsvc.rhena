
package com.unnsvc.rhena.objectserver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.objectserver.ng.ObjectServer;
import com.unnsvc.rhena.objectserver.ng.ObjectServerException;
import com.unnsvc.rhena.objectserver.ng.client.ObjectClient;
import com.unnsvc.rhena.objectserver.ng.handler.IProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.ng.handler.ProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.ng.messages.ExceptionResponse;
import com.unnsvc.rhena.objectserver.ng.messages.PingRequest;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

public class TestSocketServer {

	private SocketAddress address = new InetSocketAddress("localhost", 6666);
	private ObjectServer socketServer;

	@Before
	public void before() throws ObjectServerException {

		IProtocolHandlerFactory handlerFactory = new ProtocolHandlerFactory();
		socketServer = new ObjectServer(handlerFactory);
		socketServer.startServer(address);
	}

	@Test
	public void testUnimplementedProtocol() throws ObjectServerException {

		ObjectClient client = new ObjectClient(address);

		PingRequest request = new PingRequest();
		Response response = client.submitRequest(request);
		Assert.assertTrue(response instanceof ExceptionResponse);
	}
}
