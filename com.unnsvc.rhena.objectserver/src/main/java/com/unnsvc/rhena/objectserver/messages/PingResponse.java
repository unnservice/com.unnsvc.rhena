
package com.unnsvc.rhena.objectserver.messages;

public class PingResponse implements IResponse {

	private static final long serialVersionUID = 1L;
	private int id;

	public PingResponse(int id) {

		this.id = id;
	}

	public int getId() {

		return id;
	}
}
