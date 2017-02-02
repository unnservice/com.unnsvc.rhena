
package com.unnsvc.rhena.agent.lifecycle;

import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class LifecycleExecutionResult implements ILifecycleExecutionResult {

	private static final long serialVersionUID = 1L;
	private List<IArtifactDescriptor> generatedArtifacts;
	private List<IResource> inputs;

	public LifecycleExecutionResult(List<IArtifactDescriptor> generatedArtifacts, List<IResource> inputs) {

		this.generatedArtifacts = generatedArtifacts;
		this.inputs = inputs;
	}

	@Override
	public List<IArtifactDescriptor> getGeneratedArtifacts() {

		return generatedArtifacts;
	}

	@Override
	public List<IResource> getInputs() {

		return inputs;
	}
}
