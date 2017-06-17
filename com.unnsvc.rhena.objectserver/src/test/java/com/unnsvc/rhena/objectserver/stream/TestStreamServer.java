
package com.unnsvc.rhena.objectserver.stream;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;

public class TestStreamServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketServer server;
	private SocketClient client;

	@Before
	public void before() throws Exception {

		InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9000);

		server = new SocketServer();
		server.start(endpoint);

		client = new SocketClient();
		client.connect(endpoint);
	}

	@Test
	public void test() throws Exception {

		IResponse response = client.sendRequest(new TestRequest());

		log.info("Received reply: " + response);
	}

	@After
	public void after() throws Exception {

		client.stop();
		server.stop();
	}
}
