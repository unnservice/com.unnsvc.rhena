
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

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts, Calendar executionDate,
			List<IResource> inputs) {

		super(moduleIdentifier, executionType, artifacts, executionDate);
		this.inputs = inputs;
	}

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts, List<IResource> inputs) {

		super(moduleIdentifier, executionType, artifacts);
		this.inputs = inputs;
	}

	public WorkspaceExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts) {

		super(moduleIdentifier, executionType, artifacts);
		this.inputs = new ArrayList<IResource>();
	}

	public List<IResource> getInputs() {

		return inputs;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((inputs == null) ? 0 : inputs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkspaceExecution other = (WorkspaceExecution) obj;
		if (inputs == null) {
			if (other.inputs != null)
				return false;
		} else if (!inputs.equals(other.inputs))
			return false;
		return true;
	}

}
