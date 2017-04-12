
package com.unnsvc.rhena.common.agent;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.objectserver.IObjectReply;

public interface ILifecycleExecutionResult extends IObjectReply, Serializable {

	public List<IArtifactDescriptor> getGeneratedArtifacts();

	public List<IResource> getInputs();

}
