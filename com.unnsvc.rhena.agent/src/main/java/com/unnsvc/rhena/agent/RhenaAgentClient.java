
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.objectserver.old.ObjectServerException;
import com.unnsvc.rhena.objectserver.old.client.ObjectClient;

public class RhenaAgentClient extends ObjectClient<IExecutionRequest, IExecutionResult> implements IRhenaAgentClient {

	public RhenaAgentClient(SocketAddress socketAddress, int timeout) throws ObjectServerException {

		super(socketAddress, timeout);
	}

}
