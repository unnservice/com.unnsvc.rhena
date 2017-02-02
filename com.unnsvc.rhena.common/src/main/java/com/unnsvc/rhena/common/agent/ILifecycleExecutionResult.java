
package com.unnsvc.rhena.common.agent;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.lifecycle.IResource;

public interface ILifecycleExecutionResult extends Serializable {

	public List<IArtifactDescriptor> getGeneratedArtifacts();

	public List<IResource> getInputs();

}
