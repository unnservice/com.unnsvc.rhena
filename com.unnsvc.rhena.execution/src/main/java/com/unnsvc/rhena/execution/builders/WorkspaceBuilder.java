
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.execution.requests.ExecutionRequest;
import com.unnsvc.rhena.execution.requests.ExecutionResult;

public class WorkspaceBuilder extends AbstractBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;
	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public WorkspaceBuilder(IRhenaContext context, IEntryPoint entryPoint, IRhenaModule module) {

		this.context = context;
		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IExecutionResult call() throws Exception {

		log.info("Submitting for execution: " + module.getIdentifier());

		ExecutionRequest request = new ExecutionRequest();

		// throw new Exception("Exception");
		return new ExecutionResult(entryPoint, module);
	}

}
