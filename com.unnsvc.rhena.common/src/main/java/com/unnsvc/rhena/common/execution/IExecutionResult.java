
package com.unnsvc.rhena.common.execution;

import java.util.List;

import com.unnsvc.rhena.common.artifact.IArtifact;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.objectserver.messages.IResponse;

public interface IExecutionResult extends IResponse {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

	public List<IArtifact> getArtifacts();

}
