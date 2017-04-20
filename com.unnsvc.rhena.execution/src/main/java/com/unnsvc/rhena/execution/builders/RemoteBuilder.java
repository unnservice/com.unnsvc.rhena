
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.execution.requests.ExecutionResult;

public class RemoteBuilder extends AbstractBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public RemoteBuilder(IEntryPoint entryPoint, IRhenaModule module) {

		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IExecutionResult call() throws Exception {

		log.info("Executing: " + module.getIdentifier());

		return new ExecutionResult(entryPoint, module);
	}

}
