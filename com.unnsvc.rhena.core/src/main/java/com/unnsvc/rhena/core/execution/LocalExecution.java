
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class LocalExecution extends AbstractExecution {

	public LocalExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate) {

		super(moduleIdentifier, executionType, artifact, executionDate);
	}

	public LocalExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact) {

		super(moduleIdentifier, executionType, artifact);
	}
}
