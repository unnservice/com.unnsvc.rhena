
package com.unnsvc.rhena.objectserver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.ng.IObjectServer;
import com.unnsvc.rhena.objectserver.ng.ObjectServer;
import com.unnsvc.rhena.objectserver.ng.ObjectServerException;
import com.unnsvc.rhena.objectserver.ng.client.ObjectClient;
import com.unnsvc.rhena.objectserver.ng.handler.IProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.ng.handler.ProtocolHandlerFactory;
import com.unnsvc.rhena.objectserver.ng.messages.PingRequest;
import com.unnsvc.rhena.objectserver.ng.messages.PingResponse;
import com.unnsvc.rhena.objectserver.ng.messages.Response;

public class TestSocketServer {

	private Logger log = LoggerFactory.getLogger(getClass());
	private SocketAddress address = new InetSocketAddress("localhost", 6666);
	private IObjectServer socketServer;

	@Before
	public void before() throws ObjectServerException {

		IProtocolHandlerFactory handlerFactory = new ProtocolHandlerFactory();
		socketServer = new ObjectServer(handlerFactory);
		socketServer.startServer(address);
	}

	@After
	public void after() throws ObjectServerException {

		log.debug("after()");
		socketServer.stopServer();
	}

	@Test
	public void testPingRequest() throws ObjectServerException {

		ObjectClient client = new ObjectClient(address);

		PingRequest request = new PingRequest();
		Response response = client.submitRequest(request);
		Assert.assertTrue(response instanceof PingResponse);
		Assert.assertEquals(request.getId(), ((PingResponse) response).getId());
	}

	@Test
	public void testRepeating() throws Exception {

		ExecutorService executor = Executors.newFixedThreadPool(5);
		ObjectClient client = new ObjectClient(address);

		int NR = 100;
		for (int i = 0; i < NR; i++) {

			final int curr = i;
			executor.submit(new Runnable() {

				public void run() {

					try {

						PingRequest request = new PingRequest();
						Response response = client.submitRequest(request);
						Assert.assertTrue(response instanceof PingResponse);
						Assert.assertEquals(request.getId(), ((PingResponse) response).getId());
						log.debug("Processed " + curr + "/" + NR);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	}
}
