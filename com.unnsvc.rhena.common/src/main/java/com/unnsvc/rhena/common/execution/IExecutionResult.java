
package com.unnsvc.rhena.common.execution;

import java.util.List;

import com.unnsvc.rhena.common.artifact.IArtifact;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.objectserver.old.IObjectReply;

public interface IExecutionResult extends IObjectReply {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

	public List<IArtifact> getArtifacts();

}
