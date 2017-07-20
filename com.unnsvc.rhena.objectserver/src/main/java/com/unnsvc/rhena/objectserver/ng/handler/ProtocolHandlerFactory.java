
package com.unnsvc.rhena.objectserver.ng.handler;

public class ProtocolHandlerFactory implements IProtocolHandlerFactory {

	@Override
	public IProtocolHandler newProtocolHandler() {

		IProtocolHandler handler = new ProtocolHandler();
		return handler;
	}

}
