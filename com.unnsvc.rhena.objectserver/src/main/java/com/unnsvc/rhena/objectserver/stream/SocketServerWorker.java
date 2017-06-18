
package com.unnsvc.rhena.objectserver.stream;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.objectserver.stream.messaging.ExceptionResponse;
import com.unnsvc.rhena.objectserver.stream.messaging.IApplicationRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IControlRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.IResponse;
import com.unnsvc.rhena.objectserver.stream.messaging.PingRequest;
import com.unnsvc.rhena.objectserver.stream.messaging.PingResponse;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandler;
import com.unnsvc.rhena.objectserver.stream.protocol.IObjectProtocolHandlerFactory;

/**
 * SocketServer accepts connections and hands off to these workers which handle
 * the connection accept. These workers will handle multiple requests so they
 * will also have an event loop that is abortable on connection close
 * 
 * @author noname
 *
 */
public class SocketServerWorker extends AbstractSocketServerWorker {

	private Logger log = LoggerFactory.getLogger(getClass());

	public SocketServerWorker(Socket clientConnection, IObjectProtocolHandlerFactory protocolFactory) {

		super(clientConnection, protocolFactory);
	}

	@Override
	protected void onRequest(IRequest request) throws ConnectionException {

		try {
			if (request instanceof IControlRequest) {

				onControlRequest(request);
			} else if (request instanceof IApplicationRequest) {

				IObjectProtocolHandler objectProtocolHandler = getProtocolFactory().newObjectProtocolHandler();
				IResponse response = objectProtocolHandler.handleRequest(request);

				log.info("Writing reply");
				sendReply(response);
			} else {
				
				throw new ConnectionException("Unknown request type");
			}
		} catch (Exception ex) {

			// submit reply

			log.info("Writing exception reply");
			sendReply(new ExceptionResponse(ex));
		}
	}

	protected void onControlRequest(IRequest request) throws ConnectionException {

		if (request instanceof PingRequest) {
			sendReply(new PingResponse());
		} else {

			// unknown request
			throw new ConnectionException("Unknown request type: " + request.getClass());
		}
	}
}
