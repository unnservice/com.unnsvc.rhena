
package com.unnsvc.rhena.objectserver.stream;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.misc.ObjectProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.stream.misc.SuccessfulResponse;
import com.unnsvc.rhena.objectserver.stream.misc.TestRequest;

public class TestStreamServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ISocketServer server;
	private ISocketClient client;

	@Before
	public void before() throws Exception {

		InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9000);

		server = new SocketServer(new ObjectProtocolHandlerFactory());
		server.startServer(endpoint);

		client = new SocketClient();
		client.connect(endpoint);
	}

	@Test
	public void test() throws Exception {

		for (int i = 0; i < 10; i++) {

			IResponse response = client.sendRequest(new TestRequest(), ERequestChannel.APPLICATION);
			Assert.assertTrue(response instanceof SuccessfulResponse);
			log.info("Received reply: " + response);
		}

		synchronized (this) {
			
			this.wait(5000);
		}

	}

	@After
	public void after() throws Exception {

		client.stop();
		server.stopServer();
	}
}
