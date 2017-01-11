
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;
import java.util.Calendar;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaExecution extends Serializable {

	public ModuleIdentifier getModuleIdentifier();

	public IArtifactDescriptor getArtifact();

	public Calendar getExecutionDate();

	public EExecutionType getExecutionType();

}
