
package com.unnsvc.rhena.common.execution;

import java.util.Calendar;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaExecution {

	public ModuleIdentifier getModuleIdentifier();

	public IArtifactDescriptor getArtifact();

	public Calendar getExecutionDate();

	public EExecutionType getExecutionType();

}
