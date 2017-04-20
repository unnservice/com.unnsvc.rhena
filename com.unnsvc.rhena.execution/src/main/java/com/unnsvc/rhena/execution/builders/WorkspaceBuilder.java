
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.execution.requests.ExecutionResult;

public class WorkspaceBuilder extends AbstractBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private EExecutionType type;
	private IRhenaModule module;

	public WorkspaceBuilder(EExecutionType type, IRhenaModule module) {

		this.type = type;
		this.module = module;
	}

	@Override
	public IExecutionResult call() throws Exception {

		log.info("Executing: " + module.getIdentifier());

		return new ExecutionResult(type, module);
	}

}
