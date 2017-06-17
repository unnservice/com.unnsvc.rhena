
package com.unnsvc.rhena.objectserver.nio;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.Callable;

public abstract class AbstractConnectionThread<T extends SelectableChannel> implements Callable<Void> {

	public abstract T start(SocketAddress endpoint) throws ConnectionException;

	public abstract void close() throws ConnectionException;
}
