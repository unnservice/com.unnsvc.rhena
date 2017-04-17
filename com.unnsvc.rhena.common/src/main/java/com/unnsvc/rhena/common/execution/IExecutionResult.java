package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;

public interface IExecutionResult {

	public EExecutionType getType();

	public ModuleIdentifier getIdentifier();

}
