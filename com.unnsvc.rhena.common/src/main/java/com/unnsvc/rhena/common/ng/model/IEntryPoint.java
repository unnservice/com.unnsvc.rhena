package com.unnsvc.rhena.common.ng.model;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;

public interface IEntryPoint {

	public EExecutionType getExecutionType();

	public void setExecutionType(EExecutionType executionType);

	public ModuleIdentifier getTarget();

	public void setTarget(ModuleIdentifier target);

}
