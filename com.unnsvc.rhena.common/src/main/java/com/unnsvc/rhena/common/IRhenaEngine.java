package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaEngine {

	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException;

	public IExecutionResult resolveExecution(EExecutionType type, ModuleIdentifier identifier) throws RhenaException;

	public IExecutionResult resolveExecution(EExecutionType type, IRhenaModule module) throws RhenaException;

}
