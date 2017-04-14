
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

/**
 * @TODO Implement file content type so we know the generated artifact type
 * @author noname
 *
 */
public class RemoteExecution extends AbstractExecution {

	private static final long serialVersionUID = 1L;

	public RemoteExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts, Calendar executionDate) {

		super(moduleIdentifier, executionType, artifacts, executionDate);
	}

	public RemoteExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, List<IArtifactDescriptor> artifacts) {

		super(moduleIdentifier, executionType, artifacts);
	}
}
