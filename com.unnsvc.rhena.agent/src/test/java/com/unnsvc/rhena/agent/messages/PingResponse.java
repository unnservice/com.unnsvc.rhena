
package com.unnsvc.rhena.agent.messages;

import java.util.List;

import com.unnsvc.rhena.common.artifact.IArtifact;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class PingResponse implements IExecutionResponse {

	private static final long serialVersionUID = 1L;

	@Override
	public IEntryPoint getEntryPoint() {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public IRhenaModule getModule() {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<IArtifact> getArtifacts() {

		throw new UnsupportedOperationException("not implemented");
	}

}
