
package com.unnsvc.rhena.agent.requests;

import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.objectserver.IObjectReply;

public class LifecycleExecutionResult implements ILifecycleExecutionResult, IObjectReply {

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
