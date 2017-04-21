
package com.unnsvc.rhena.agent;

import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectReply;
import com.unnsvc.rhena.objectserver.IObjectRequest;
import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.server.ObjectServer;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

public class RhenaAgent implements IRhenaAgent {

	private IObjectServer objectServer;

	public RhenaAgent() throws RhenaException {

		try {

			objectServer = new ObjectServer(ObjectServerHelper.availableAddress()) {

				@Override
				protected void onStarted() {

					synchronized (objectServer) {
						
						objectServer.notifyAll();
					}
				}
			};
		} catch (ObjectServerException ose) {

			throw new RhenaException(ose);
		}
	}

	@Override
	public void start() throws RhenaException {

		try {
			synchronized (objectServer) {
				objectServer.startServer(new IObjectServerAcceptor() {

					@Override
					public IObjectReply onRequest(IObjectRequest request) {

						throw new UnsupportedOperationException("Not implemented");
					}

					@Override
					public int getSocketReadTimeout() {

						throw new UnsupportedOperationException("Not implemented");
					}
				});
				objectServer.wait();
			}
		} catch (ObjectServerException | InterruptedException ex) {

			throw new RhenaException(ex);
		}
	}

	@Override
	public void close() throws RhenaException {

		try {
			objectServer.close();
		} catch (ObjectServerException ose) {

			throw new RhenaException(ose);
		}
	}
}
