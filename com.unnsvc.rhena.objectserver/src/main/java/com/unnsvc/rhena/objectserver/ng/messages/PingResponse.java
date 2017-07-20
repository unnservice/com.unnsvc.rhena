
package com.unnsvc.rhena.objectserver.ng.messages;

public class PingResponse implements Response {

	private static final long serialVersionUID = 1L;
	private int id;

	public PingResponse(int id) {

		this.id = id;
	}

	public int getId() {

		return id;
	}
}
