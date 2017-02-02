
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifact;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class LocalExecution extends AbstractExecution {

	private static final long serialVersionUID = 1L;

	public LocalExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts, Calendar executionDate) {

		super(moduleIdentifier, executionType, artifacts, executionDate);
	}

	public LocalExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts) {

		super(moduleIdentifier, executionType, artifacts);
	}
}
