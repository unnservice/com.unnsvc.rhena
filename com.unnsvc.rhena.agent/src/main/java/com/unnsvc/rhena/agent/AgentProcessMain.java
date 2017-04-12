
package com.unnsvc.rhena.agent;

import java.net.InetSocketAddress;

import com.unnsvc.rhena.agent.requests.ExceptionReply;
import com.unnsvc.rhena.common.agent.IExecutionRequest;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectReply;
import com.unnsvc.rhena.objectserver.IObjectRequest;
import com.unnsvc.rhena.objectserver.IObjectServer;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;
import com.unnsvc.rhena.objectserver.server.ObjectServer;

public class AgentProcessMain {
	
	public static final int AGENT_DEFAULT_PORT = 6789;

	public static void main(String... args) throws Exception {

		int executionPort = Integer.valueOf(args[0]);
		IObjectServer server = new ObjectServer(new InetSocketAddress(executionPort));
		server.startServer(new IObjectServerAcceptor() {

			@Override
			public IObjectReply onRequest(IObjectRequest request) {

				try {
					LifecycleAgent agent = new LifecycleAgent();
					IExecutionRequest execReq = (IExecutionRequest) request;
					ILifecycleExecutionResult result = agent.executeLifecycle(execReq);
					return result;
				} catch (RhenaException re) {
					return new ExceptionReply(re);
				}
			}

			@Override
			public int getSocketReadTimeout() {

				return 1000;
			}
		});
	}
}
