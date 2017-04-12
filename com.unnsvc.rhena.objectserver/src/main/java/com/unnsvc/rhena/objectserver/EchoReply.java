
package com.unnsvc.rhena.objectserver;

public class EchoReply implements IReply {

	private IRequest request;

	public EchoReply(IRequest request) {

		this.request = request;
	}

	public IRequest getEchoReply() {

		return request;
	}

}
