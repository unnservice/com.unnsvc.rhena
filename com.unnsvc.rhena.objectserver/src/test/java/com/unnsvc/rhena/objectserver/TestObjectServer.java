
package com.unnsvc.rhena.objectserver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.objectserver.client.ObjectClient;
import com.unnsvc.rhena.objectserver.server.ObjectServer;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

public class TestObjectServer {

	private IObjectServer server;

	@Before
	public void before() throws Exception {

		server = new ObjectServer(ObjectServerHelper.availableAddress());

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					server.startServer(new IObjectServerAcceptor() {

						@Override
						public IObjectReply onRequest(IObjectRequest request) {

							return new EchoReply(request);
						}

						@Override
						public int getSocketReadTimeout() {

							return 1000;
						}
					});
				} catch (Exception ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}

		}).start();
	}

	@After
	public void after() throws ObjectServerException {

		server.close();
	}

	@Test
	public void testObjectServer() throws Exception {

		IObjectClient client = new ObjectClient(server.getServerAddress());

		for (int i = 0; i < 10; i++) {
			EchoReply reply = (EchoReply) client.executeRequest(new TestRequest());
			Assert.assertTrue(reply.getEchoReply() instanceof TestRequest);
		}
	}
}
