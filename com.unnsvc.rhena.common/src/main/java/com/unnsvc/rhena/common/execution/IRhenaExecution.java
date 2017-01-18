
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaExecution extends Serializable {

	public ModuleIdentifier getModuleIdentifier();

	public List<IArtifactDescriptor> getArtifacts();

	public Calendar getExecutionDate();

	public EExecutionType getExecutionType();

}
