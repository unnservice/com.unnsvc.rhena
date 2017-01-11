
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class WorkspaceExecution extends AbstractExecution {

	private static final long serialVersionUID = 1L;

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate) {

		super(moduleIdentifier, executionType, artifact, executionDate);
	}

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact) {

		super(moduleIdentifier, executionType, artifact);
	}
}
