
package com.unnsvc.rhena.objectserver.messages;

import java.util.Random;

public class PingRequest implements Request {

	private static final long serialVersionUID = 1L;
	private int id;

	public PingRequest() {

		this.id = new Random().nextInt();
	}

	public int getId() {

		return id;
	}
}
