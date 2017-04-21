
package com.unnsvc.rhena.agent;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.client.ObjectClient;

public class RhenaAgentClient extends ObjectClient<IExecutionRequest, IExecutionResult> implements IRhenaAgentClient {

	public RhenaAgentClient(SocketAddress socketAddress) throws ObjectServerException {

		super(socketAddress);
	}

}
