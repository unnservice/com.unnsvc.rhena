
package com.unnsvc.rhena.agent;

import java.util.Collections;
import java.util.List;

import com.unnsvc.rhena.common.artifact.IArtifact;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionResult implements IExecutionResult {

	private static final long serialVersionUID = 1L;
	private IEntryPoint entryPoint;
	private IRhenaModule module;
	private List<IArtifact> artifacts;

	public ExecutionResult(IEntryPoint entryPoint, IRhenaModule module, List<IArtifact> artifacts) {

		this.entryPoint = entryPoint;
		this.module = module;
		this.artifacts = artifacts;
	}

	public ExecutionResult(IEntryPoint entryPoint, IRhenaModule module) {

		this(entryPoint, module, Collections.EMPTY_LIST);
	}

	@Override
	public List<IArtifact> getArtifacts() {

		return artifacts;
	}

	@Override
	public IEntryPoint getEntryPoint() {

		return entryPoint;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}
}
