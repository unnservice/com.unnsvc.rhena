
package com.unnsvc.rhena.core.execution;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class WorkspaceExecution extends AbstractExecution {

	private static final long serialVersionUID = 1L;
	private List<IResource> inputs;

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate,
			List<IResource> inputs) {

		super(moduleIdentifier, executionType, artifact, executionDate);
		this.inputs = inputs;
	}

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, List<IResource> inputs) {

		super(moduleIdentifier, executionType, artifact);
		this.inputs = inputs;
	}

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact) {

		super(moduleIdentifier, executionType, artifact);
		this.inputs = new ArrayList<IResource>();
	}

	public List<IResource> getInputs() {

		return inputs;
	}
}
