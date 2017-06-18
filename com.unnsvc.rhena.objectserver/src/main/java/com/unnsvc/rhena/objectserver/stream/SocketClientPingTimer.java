
package com.unnsvc.rhena.objectserver.stream;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.messaging.PingRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.PingResponse;

public abstract class SocketClientPingTimer extends TimerTask {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ISocketClient client;
	private Timer timer;

	public SocketClientPingTimer(ISocketClient client) {

		this.client = client;
		this.timer = new Timer();
	}

	@Override
	public void run() {

		try {
			log.info("Sending ping request");
			IResponse response = client.sendRequest(new PingRequest());
			log.info("Received response " + response);
			if (!(response instanceof PingResponse)) {
				onFailure();
			}
		} catch (ConnectionTimeoutException cte) {

			log.error("Ping timeout", cte);
		} catch (ConnectionException e) {

			log.error(e.getMessage(), e);
		}
	}

	public abstract void onFailure();

	public void startTimer(int periodMs) {

		timer.scheduleAtFixedRate(this, 0, periodMs);
	}
}
