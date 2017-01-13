
package com.unnsvc.rhena.agent.lifecycle;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class LifecycleExecutionResult implements ILifecycleExecutionResult {

	private static final long serialVersionUID = 1L;
	private File generatedArtifact;
	private List<IResource> inputs;

	public LifecycleExecutionResult(File generatedArtifact, List<IResource> inputs) {

		this.generatedArtifact = generatedArtifact;
		this.inputs = inputs;
	}

	@Override
	public File getGeneratedArtifact() {

		return generatedArtifact;
	}

	@Override
	public List<IResource> getInputs() {

		return inputs;
	}
}
