
package com.unnsvc.rhena.common;

import java.net.SocketAddress;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;

public interface IRhenaAgentClientFactory {

	public IRhenaAgentClient<IExecutionRequest, IExecutionResponse> newClient(SocketAddress endpoint) throws RhenaException;

}
