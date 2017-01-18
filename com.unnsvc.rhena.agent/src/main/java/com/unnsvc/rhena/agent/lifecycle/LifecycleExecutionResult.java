
package com.unnsvc.rhena.agent.lifecycle;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class LifecycleExecutionResult implements ILifecycleExecutionResult {

	private static final long serialVersionUID = 1L;
	private List<File> generatedArtifacts;
	private List<IResource> inputs;

	public LifecycleExecutionResult(List<File> generatedArtifacts, List<IResource> inputs) {

		this.generatedArtifacts = generatedArtifacts;
		this.inputs = inputs;
	}

	@Override
	public List<File> getGeneratedArtifacts() {

		return generatedArtifacts;
	}

	@Override
	public List<IResource> getInputs() {

		return inputs;
	}
}
