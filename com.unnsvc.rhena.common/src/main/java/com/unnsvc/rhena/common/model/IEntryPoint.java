package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IEntryPoint {

	public EExecutionType getExecutionType();

	public void setExecutionType(EExecutionType executionType);

	public ModuleIdentifier getTarget();

	public void setTarget(ModuleIdentifier target);

}
