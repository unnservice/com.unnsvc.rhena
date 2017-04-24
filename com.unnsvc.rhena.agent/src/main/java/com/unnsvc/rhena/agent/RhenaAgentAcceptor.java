
package com.unnsvc.rhena.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;

public class RhenaAgentAcceptor implements IObjectServerAcceptor<IExecutionRequest, IExecutionResult> {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public IExecutionResult onRequest(IExecutionRequest request) {

		log.debug("Executing in agent, returning result for " + request.getEntryPoint());
		IExecutionResult result =  new ExecutionResult(request.getEntryPoint(), request.getModule());
		
		build(request);
		
		return result;
	}

	protected void build(IExecutionRequest request) {

	}

	@Override
	public int getServerSocketReadTimeout() {

		return 1000;
	}

}
