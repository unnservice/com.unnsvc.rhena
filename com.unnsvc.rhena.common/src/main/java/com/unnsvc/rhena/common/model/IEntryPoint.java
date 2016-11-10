package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IEntryPoint {

	public EExecutionType getExecutionType();

	public ModuleIdentifier getTarget();
}
