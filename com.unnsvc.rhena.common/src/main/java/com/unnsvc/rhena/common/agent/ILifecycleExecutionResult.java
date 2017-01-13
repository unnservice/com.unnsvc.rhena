
package com.unnsvc.rhena.common.agent;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.lifecycle.IResource;

public interface ILifecycleExecutionResult extends Serializable {

	public File getGeneratedArtifact();

	public List<IResource> getInputs();

}
