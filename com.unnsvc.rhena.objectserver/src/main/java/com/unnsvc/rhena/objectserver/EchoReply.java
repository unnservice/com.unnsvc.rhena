
package com.unnsvc.rhena.objectserver;

public class EchoReply implements IObjectReply {

	private static final long serialVersionUID = 1L;
	private IObjectRequest request;

	public EchoReply(IObjectRequest request) {

		this.request = request;
	}

	public IObjectRequest getEchoReply() {

		return request;
	}

}
