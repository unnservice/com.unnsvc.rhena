
package com.unnsvc.rhena.agent.lifecycle;

import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.execution.IResult;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class LifecycleExecutionResult implements ILifecycleExecutionResult {

	private static final long serialVersionUID = 1L;
	private List<IResult> generatedArtifacts;
	private List<IResource> inputs;

	public LifecycleExecutionResult(List<IResult> generatedArtifacts, List<IResource> inputs) {

		this.generatedArtifacts = generatedArtifacts;
		this.inputs = inputs;
	}

	@Override
	public List<IResult> getGeneratedArtifacts() {

		return generatedArtifacts;
	}

	@Override
	public List<IResource> getInputs() {

		return inputs;
	}
}
