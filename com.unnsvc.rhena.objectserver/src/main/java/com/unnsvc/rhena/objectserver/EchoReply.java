
package com.unnsvc.rhena.objectserver;

public class EchoReply implements IObjectReply {

	private IObjectRequest request;

	public EchoReply(IObjectRequest request) {

		this.request = request;
	}

	public IObjectRequest getEchoReply() {

		return request;
	}

}
