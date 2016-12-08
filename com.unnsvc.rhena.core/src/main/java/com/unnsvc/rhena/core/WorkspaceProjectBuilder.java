
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class WorkspaceProjectBuilder {

	private IModelResolver resolver;

	public WorkspaceProjectBuilder(IModelResolver resolver) {

		this.resolver = resolver;
	}

	public IRhenaExecution produceExecution(IRhenaModule module, EExecutionType executionType) throws RhenaException {

		return new RhenaExecution(module.getIdentifier(), executionType, new ArtifactDescriptor("someartifact", "http://some.url.com/", "not-implemented"));
	}

}
