
package com.unnsvc.rhena.objectserver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.objectserver.client.ObjectClient;
import com.unnsvc.rhena.objectserver.server.ObjectServer;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

@SuppressWarnings("rawtypes")
public class TestObjectServer {

	private IObjectServer server;

	@Before
	public void before() throws Exception {

		server = new ObjectServer(ObjectServerHelper.availableAddress()) {

			@Override
			public IObjectServerAcceptor newAcceptor() {

				return new IObjectServerAcceptor() {

					@Override
					public IObjectReply onRequest(IObjectRequest request) {

						return new EchoReply(request);
					}

					@Override
					public int getServerSocketReadTimeout() {

						return 1000;
					}
				};
			}

		};

		server.startServer();
	}

	@After
	public void after() throws ObjectServerException {

		server.close();
	}

	@Test
	public void testObjectServer() throws Exception {

		for (int i = 0; i < 5; i++) {
			IObjectClient client = new ObjectClient(server.getServerAddress(), 1000);
			EchoReply reply = (EchoReply) client.executeRequest(new TestRequest());
			Assert.assertTrue(reply.getEchoReply() instanceof TestRequest);
			client.close();
		}

	}
}
