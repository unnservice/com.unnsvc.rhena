
package com.unnsvc.rhena.objectserver.handler;

public class ProtocolHandlerFactory implements IProtocolHandlerFactory {

	@Override
	public IProtocolHandler newProtocolHandler() {

		IProtocolHandler handler = new ProtocolHandler();
		return handler;
	}

}
