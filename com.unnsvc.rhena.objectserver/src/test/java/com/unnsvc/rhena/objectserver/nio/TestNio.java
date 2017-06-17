
package com.unnsvc.rhena.objectserver.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.nio.messages.IRequest;

@Ignore
public class TestNio {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ExecutorService executor;
	private ServerConnectionThread serverThread;
	private ClientConnectionThread clientThread;

	@Before
	public void before() throws Exception {

		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		serverThread = new ServerConnectionThread(executor);
		serverThread.start(null);
	}

	@Test
	public void test() throws Exception {

		clientThread = new ClientConnectionThread(executor);
		log.info("Starting client");
		clientThread.start(serverThread.getServerChannel().getLocalAddress());

		log.info("Started, submitting request");
		clientThread.submit(new IRequest() {
		});
	}

	@After
	public void after() throws Exception {

		clientThread.close();
		serverThread.close();
	}
}
