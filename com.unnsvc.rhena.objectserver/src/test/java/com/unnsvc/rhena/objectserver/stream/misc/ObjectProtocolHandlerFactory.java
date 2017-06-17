
package com.unnsvc.rhena.objectserver.stream.misc;

import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

public class ObjectProtocolHandlerFactory implements IObjectProtocolHandlerFactory {

	@Override
	public IObjectProtocolHandler newObjectProtocolHandler() {

		return new ObjectProtocolHandler();
	}

}
