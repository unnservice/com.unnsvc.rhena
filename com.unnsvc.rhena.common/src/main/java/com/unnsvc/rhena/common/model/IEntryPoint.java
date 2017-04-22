
package com.unnsvc.rhena.common.model;

import java.io.Serializable;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IEntryPoint extends Serializable {

	public EExecutionType getExecutionType();

	public void setExecutionType(EExecutionType executionType);

	public ModuleIdentifier getTarget();

	public void setTarget(ModuleIdentifier target);

}
