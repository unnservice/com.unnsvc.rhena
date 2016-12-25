
package com.unnsvc.rhena.core.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

/**
 * @TODO Implement file content type so we know the generated artifact type
 * @author noname
 *
 */
public class RemoteExecution extends AbstractExecution {

	public RemoteExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact, Calendar executionDate) {

		super(moduleIdentifier, executionType, artifact, executionDate);
	}

	public RemoteExecution(ModuleIdentifier moduleIdentifier, EExecutionType executionType, IArtifactDescriptor artifact) {

		super(moduleIdentifier, executionType, artifact);
	}
}
