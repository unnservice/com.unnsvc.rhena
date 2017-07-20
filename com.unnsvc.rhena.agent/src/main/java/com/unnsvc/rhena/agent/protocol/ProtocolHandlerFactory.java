
package com.unnsvc.rhena.agent.protocol;

import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandlerFactory;

public class ProtocolHandlerFactory implements IProtocolHandlerFactory {

	@Override
	public IProtocolHandler newProtocolHandler() {

		return new ProtocolHandler();
	}
}
